package net.stepbooks.domain.wdt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangdian.api.WdtClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.enums.WdtSyncStatus;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.product.service.SkuPhysicalGoodsService;
import net.stepbooks.domain.wdt.service.WdtService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.JsonUtils;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.infrastructure.util.RedisStore;
import net.stepbooks.interfaces.KeyConstants;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WdtServiceImpl implements WdtService {

    @Value("${wdt.app-key}")
    private String appkey;

    @Value("${wdt.app-secret}")
    private String appSecret;

    @Value("${wdt.sid}")
    private String sid;

    @Value("${wdt.base-url}")
    private String baseUrl;

    @Value("${wdt.shop-no}")
    private String shopNo;

    @Value("${wdt.platform-id}")
    private String platformId;

    private final PhysicalGoodsService physicalGoodsService;

    private final OrderMapper orderMapper;

    private final OrderSkuService orderSkuService;

    private final SkuPhysicalGoodsService skuPhysicalGoodsService;

    private final RedisDistributedLocker redisDistributedLocker;

    private final DeliveryService deliveryService;

    private final RedisStore redisStore;

    private final String jobName = "WdtPush";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 启动后2分钟第一次执行
     */
    private final long initialDelay = 120000L;

    /**
     * 每15分钟执行一次
     */
    private final long fixedDelay = 900000L;

    private final long two = 2L;

    /**
     * 一次性最多同步货品数
     */
    private final long maxGoodsSize = 1900L;

    /**
     * 一次性最多同步订单数
     */
    private final long maxTradeSize = 10L;

    /**
     * 旺店通的已支付，待发货状态常量
     */
    private final int wdtTradeStatusPaid = 30;

    /**
     * 旺店通支付状态-已付款
     */
    private final int wdtPayStatusPaid = 2;

    /**
     * 旺店通发货条件-款到发货
     */
    private final int wdtDeliveryTermKdfh = 1;

    /**
     * 初始库存
     */
    private final int wdtInitStockNo = 99999;

    /**
     * 最大物流信息同步数量
     */
    private final String logisticsSyncLimit = "20";

    protected void goodsSpecPushImpl() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastActTime = redisStore.get(KeyConstants.LAST_ACT_TIME_WDT_SYNC, LocalDateTime.class);

        if (lastActTime != null && lastActTime.plusMinutes(two).isAfter(now)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请求太频繁，稍后再试");
        }

        redisStore.set(KeyConstants.LAST_ACT_TIME_WDT_SYNC, now);

        LocalDateTime lastModifyAt = redisStore.get(KeyConstants.LAST_MODIFY_AT_WDT_SYNC, LocalDateTime.class);
        LambdaQueryWrapper<PhysicalGoodsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.isNotNull(PhysicalGoodsEntity::getWdtGoodsNo);
        if (lastModifyAt != null) {
            wrapper.gt(PhysicalGoodsEntity::getModifiedAt, lastModifyAt);
        }
        wrapper.orderByAsc(PhysicalGoodsEntity::getModifiedAt);

        Page<PhysicalGoodsEntity> page = Page.of(1, maxGoodsSize);
        IPage<PhysicalGoodsEntity> physicalGoods = physicalGoodsService.page(page, wrapper);
        if (physicalGoods.getTotal() <= 0) {
            log.info("no goods update, stop it");
            return;
        }

        List<PhysicalGoodsEntity> toSyncList = physicalGoods.getRecords();

        WdtClient client = new WdtClient(sid, appkey, appSecret, baseUrl);
        Map<String, Object> apiGoodsInfo = new HashMap<>();
        Map<String, Object>[] goodsList = new Map[toSyncList.size()];

        for (int i = 0; i < toSyncList.size(); i++) {
            PhysicalGoodsEntity physicalGoodsEntity = toSyncList.get(i);
            goodsList[i] = new HashMap<>();
            String wdtGoodsNo = physicalGoodsEntity.getWdtGoodsNo();
            goodsList[i].put("goods_id", wdtGoodsNo);
            goodsList[i].put("spec_id", wdtGoodsNo);
            goodsList[i].put("goods_no", wdtGoodsNo);
            goodsList[i].put("spec_no", wdtGoodsNo);
            goodsList[i].put("goods_name", physicalGoodsEntity.getName());
            goodsList[i].put("pic_url", physicalGoodsEntity.getCoverUrl());
            goodsList[i].put("status", "1");
            goodsList[i].put("stock_num", wdtInitStockNo);
            lastModifyAt = physicalGoodsEntity.getModifiedAt();
        }

        apiGoodsInfo.put("platform_id", platformId);
        apiGoodsInfo.put("shop_no", shopNo);
        apiGoodsInfo.put("goods_list", goodsList);

        String apiGoodsInfoJson = JSON.toJSONString(apiGoodsInfo);

        Map<String, String> params = new HashMap<String, String>();

        params.put("api_goods_info", apiGoodsInfoJson);
        try {
            String response = client.execute("api_goodsspec_push.php", params);
            log.info("api_goodsspec_push.php response={}", response);
            WdtGoodsSpecPushResponse goodsSpecPushResponse = JsonUtils.fromJson(response,
                    WdtGoodsSpecPushResponse.class);
            if (goodsSpecPushResponse.success()) {
                redisStore.set(KeyConstants.LAST_MODIFY_AT_WDT_SYNC, lastModifyAt);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public void goodsSpecPush(boolean updateAll) {
        if (updateAll) {
            redisStore.delete(KeyConstants.LAST_MODIFY_AT_WDT_SYNC);
        }
        goodsSpecPushImpl();
    }

    @Override
    public void tradePush() {
        log.info("Trade push start ...");
        tradePushImpl();
    }

    @Override
    public void retryTradePush(String orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && WdtSyncStatus.REJECTED.equals(order.getWdtSyncStatus())) {
            order.setWdtSyncStatus(WdtSyncStatus.INIT);
            orderMapper.updateById(order);
        }
    }

    protected void logisticsSyncAck(List<String> recIds) {
        WdtClient client = new WdtClient(sid, appkey, appSecret, baseUrl);
        List<Map<String, Object>> logisticsList = new ArrayList<>();

        for (String recId : recIds) {
            Map<String, Object> logistics = new HashMap<String, Object>();
            logistics.put("rec_id", recId);
            logistics.put("status", 0);
            logistics.put("message", "同步成功");
            logisticsList.add(logistics);
        }
        String logisticsListJson = JSON.toJSONString(logisticsList);

        Map<String, String> params = new HashMap<>();
        params.put("logistics_list", logisticsListJson);
        try {
            String response = client.execute("logistics_sync_ack.php", params);
            log.info("logistics_sync_ack response={}", response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    protected void logisticsSyncQueryImpl() {
        WdtClient client = new WdtClient(sid, appkey, appSecret, baseUrl);
        Map<String, String> params = new HashMap<>();
        params.put("shop_no", shopNo);
        params.put("limit", logisticsSyncLimit);

        try {
            String response = client.execute("logistics_sync_query.php", params);
            log.info("logistics_sync_query response={}", response);
            WdtLogisticsSyncResponse logisticsSyncResponse = JSON.parseObject(response, WdtLogisticsSyncResponse.class);
            List<String> recIds = new ArrayList<>();
            if (logisticsSyncResponse.success() && logisticsSyncResponse.getTrades() != null) {
                try {
                    for (WdtTrade trade : logisticsSyncResponse.getTrades()) {
                        String orderId = trade.getTid();
                        Delivery delivery = deliveryService.getByOrder(orderId);
                        delivery.setLogisticsId(trade.getLogisticsId());
                        delivery.setLogisticsNo(trade.getLogisticsNo());
                        delivery.setLogisticsName(trade.getLogisticsName());
                        delivery.setLogisticsType(trade.getLogisticsType());
                        delivery.setConsignTime(trade.getConsignTime());
                        if (DeliveryStatus.WAITING.equals(delivery.getDeliveryStatus())) {
                            delivery.setDeliveryStatus(DeliveryStatus.DELIVERING);
                        }
                        deliveryService.updateById(delivery);

                        Order order = orderMapper.selectById(orderId);
                        if (order != null && OrderState.PAID.equals(order.getState())) {
                            order.setState(OrderState.SHIPPED);
                            orderMapper.updateById(order);
                        }

                        recIds.add(trade.getRecId());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            logisticsSyncAck(recIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public void logisticsSyncQuery() {
        log.info("Logistics sync start ...");
        logisticsSyncQueryImpl();
    }

    private static int retryTimes = 0;

    private static final int MAX_RETRY_TIMES = 10;


    /**
     * 标记订单同步成功，旺店通的tid就是我们这里的orderId
     *
     * @param tids
     */
    private void markWdtSyncSuccess(List<String> tids) {
        Order updateOrder = new Order();
        updateOrder.setWdtSyncStatus(WdtSyncStatus.DONE);
        LambdaQueryWrapper<Order> updateWrapper = Wrappers.lambdaQuery();
        updateWrapper.in(Order::getId, tids);
        orderMapper.update(updateOrder, updateWrapper);
    }

    private boolean tooManyRetryTimes() {
        if (retryTimes >= MAX_RETRY_TIMES) {
            //最多重试3次
            retryTimes = 0;
            log.info("too many retry times ...");
            return true;
        }
        return false;
    }

    public void tradePushImpl() {
        if (tooManyRetryTimes()) {
            return;
        }
        Page<Order> page = Page.of(1, maxTradeSize);
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getWdtSyncStatus, WdtSyncStatus.INIT);
        wrapper.orderByDesc(Order::getCreatedAt);
        //wrapper.eq(Order::getPaymentStatus, PaymentStatus.PAID);
        IPage<Order> orders = orderMapper.selectPage(page, wrapper);
        List<Order> noNeedOrders = new ArrayList<>();
        List<Order> toSyncOrders = new ArrayList<>();
        for (Order order : orders.getRecords()) {
            if (OrderState.PAID.equals(order.getState())) {
                toSyncOrders.add(order);
            } else if (OrderState.REFUNDING.equals(order.getState()) || OrderState.SHIPPED.equals(order.getState())
                    || OrderState.CLOSED.equals(order.getState()) || OrderState.REFUNDED.equals(order.getState())
                    || OrderState.FINISHED.equals(order.getState())) {
                noNeedOrders.add(order);
            }
        }

        if (noNeedOrders.size() > 0) {
            Order updateOrder = new Order();
            updateOrder.setWdtSyncStatus(WdtSyncStatus.NO_NEED);
            LambdaQueryWrapper<Order> updateWrapper = Wrappers.lambdaQuery();
            updateWrapper.in(Order::getId, noNeedOrders.stream().map(Order::getId).collect(Collectors.toList()));
            orderMapper.update(updateOrder, updateWrapper);
        }
        if (toSyncOrders.size() > 0) {
            WdtClient client = new WdtClient(sid, appkey, appSecret, baseUrl);
            List<Map<String, Object>> wdtTradeList = new ArrayList<>();
            List<String> toSyncTids = new ArrayList<>();
            for (Order order : toSyncOrders) {
                List<Map<String, Object>> wdtOrderList = new ArrayList<>();
                List<OrderSkuDto> orderSkuDtos = orderSkuService.findOrderSkusByOrderId(order.getId());
                //我们用Order.getId()作为旺店通接口中的tid，用OrderSkuDto.getId()作为旺店通接口的oid
                //sku理论上是可以包含多个实物商品，用OrderSkuDto.getId()+"_1"，OrderSkuDto.getId()+"_2"表示吧
                for (OrderSkuDto orderSkuDto : orderSkuDtos) {
                    String oid = orderSkuDto.getId();
                    BigDecimal skuPrice = orderSkuDto.getSkuPrice();
                    int quantity = orderSkuDto.getQuantity();
                    String skuId = orderSkuDto.getSkuId();
                    List<PhysicalGoodsEntity> goodsList = skuPhysicalGoodsService.
                            getPhysicalGoodsListBySkuId(skuId);
                    if (goodsList.size() > 1) {
                        log.warn("order {} has more than 1 physical goods!", order.getId());
                    } else if (goodsList.size() == 1) {
                        PhysicalGoodsEntity physicalGoods = goodsList.get(0);
                        String wdtGoodsNo = physicalGoods.getWdtGoodsNo();
                        if (ObjectUtils.isNotEmpty(wdtGoodsNo)) {
                            Map<String, Object> wdtOrder = new HashMap<>();
                            wdtOrder.put("oid", oid);
                            wdtOrder.put("num", quantity);
                            wdtOrder.put("price", skuPrice);
                            wdtOrder.put("status", wdtTradeStatusPaid);
                            wdtOrder.put("refund_status", 0);
                            wdtOrder.put("goods_id", wdtGoodsNo);
                            wdtOrder.put("spec_id", wdtGoodsNo);
                            wdtOrder.put("goods_no", wdtGoodsNo);
                            wdtOrder.put("spec_no", wdtGoodsNo);
                            wdtOrder.put("goods_name", physicalGoods.getName());
                            wdtOrder.put("discount", 0);         //子订单折扣
                            wdtOrder.put("adjust_amount", 0);    //手工调整,特别注意:正的表示加价,负的表示减价
                            wdtOrder.put("share_discount", 0);
                            wdtOrderList.add(wdtOrder);
                        }
                    } else {
                        log.warn("order {} has no physical goods!", order.getId());
                    }
                }

                if (wdtOrderList.size() > 0) {
                    String tid = order.getId();
                    toSyncTids.add(tid);
                    Map<String, Object> trade = new HashMap<>();
                    trade.put("tid", tid);
                    trade.put("trade_status", wdtTradeStatusPaid);
                    trade.put("pay_status", wdtPayStatusPaid);
                    trade.put("delivery_term", wdtDeliveryTermKdfh);
                    trade.put("trade_time", order.getCreatedAt().format(formatter));
                    Delivery delivery = deliveryService.getByOrder(order.getId());
                    trade.put("buyer_nick", delivery.getRecipientName());
                    trade.put("receiver_name", delivery.getRecipientName());
                    trade.put("receiver_mobile", delivery.getRecipientPhone());
                    trade.put("receiver_province", delivery.getRecipientProvince());
                    trade.put("receiver_city", delivery.getRecipientCity());
                    trade.put("receiver_district", delivery.getRecipientDistrict());
                    trade.put("receiver_address", delivery.getRecipientAddress());
                    trade.put("post_amount", BigDecimal.ZERO);
                    trade.put("cod_amount", BigDecimal.ZERO);
                    trade.put("ext_cod_fee", BigDecimal.ZERO);
                    trade.put("other_amount", BigDecimal.ZERO);
                    trade.put("paid", order.getPaymentAmount());
                    trade.put("order_list", wdtOrderList);
                    wdtTradeList.add(trade);
                }
            }
            if (wdtTradeList.size() > 0) {
                String tradeListJson = JSON.toJSONString(wdtTradeList);
                log.info("tradeListJson={}", tradeListJson);
                Map<String, String> params = new HashMap<>();
                params.put("shop_no", shopNo);
                params.put("trade_list", tradeListJson);
                try {
                    String response = client.execute("trade_push.php", params);
                    WdtTradePushResponse pushResponse = JsonUtils.fromJson(response, WdtTradePushResponse.class);
                    log.info("trade push response={}", pushResponse);
                    if (pushResponse.success()) {
                        markWdtSyncSuccess(toSyncTids);
                    } else {
                        try {
                            boolean needRetry = false;
                            List<WdtTradeError> wdtTradeErrors = JsonUtils.fromJsonArray(
                                    pushResponse.getMessage(), WdtTradeError.class);
                            for (WdtTradeError wdtTradeError : wdtTradeErrors) {
                                if (wdtTradeError != null && wdtTradeError.getTid() != null) {
                                    for (Order order : toSyncOrders) {
                                        if (order.getId().equals(wdtTradeError.getTid())) {
                                            log.warn("order {} is rejected by wdt", order.getId());
                                            order.setWdtSyncStatus(WdtSyncStatus.REJECTED);
                                            orderMapper.updateById(order);
                                            needRetry = true;
                                        }
                                    }
                                }
                            }
                            if (needRetry) {
                                retryTimes++;
                                log.info("trade push retry ...");
                                tradePushImpl();
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Scheduled(initialDelay = initialDelay, fixedDelay = fixedDelay)
    public void wdtPush() {
        boolean res = redisDistributedLocker.tryLock(jobName);
        if (!res) {
            log.info("{} Job already in progress", jobName);
            return;
        }
        try {
            log.info("{} Job start ...", jobName);
            if (!redisStore.exists(KeyConstants.FLAG_WDT_SYNC)) {
                redisStore.setWithTwoMinutesExpiration(KeyConstants.FLAG_WDT_SYNC, true);

                try {
                    log.info("Goods spec push start ...");
                    goodsSpecPushImpl();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

                try {
                    log.info("Trade push start ...");
                    tradePushImpl();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

                try {
                    log.info("Logistics sync start ...");
                    logisticsSyncQueryImpl();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

            } else {
                log.info("{} execute too frequently", jobName);
            }
        } finally {
            redisDistributedLocker.unlock(jobName);
        }
    }

}
