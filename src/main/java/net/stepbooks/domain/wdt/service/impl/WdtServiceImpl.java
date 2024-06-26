package net.stepbooks.domain.wdt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangdian.api.WdtClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.domain.wdt.service.WdtService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.JsonUtils;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.infrastructure.util.RedisStore;
import net.stepbooks.interfaces.KeyConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WdtServiceImpl implements WdtService {

    private String appkey = "bywh002-test";
    private String appsecret = "1141bde90";
    private String sid = "apidevnew2";
    private String baseUrl = "https://sandbox.wangdian.cn/openapi2/";

    private String shopNo = "bywh002-test";
    private String platformId = "127";

    private final PhysicalGoodsService physicalGoodsService;

    private final RedisDistributedLocker redisDistributedLocker;

    private final RedisStore redisStore;

    private final String jobName = "WdtGoodsSpecPush";

    /**
     * 启动后3分钟第一次执行
     */
    private final long initialDelay = 180000L;

    /**
     * 每15分钟执行一次
     */
    private final long fixedDelay = 900000L;

    private final long two = 2L;

    /**
     * 一次性最多同步条数
     */
    private final long maxPageSize = 1900L;

    protected void goodsSpecPushImpl() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastActTime = redisStore.get(KeyConstants.LAST_ACT_TIME_WDT_SYNC, LocalDateTime.class);

        if (lastActTime != null && lastActTime.plusMinutes(two).isAfter(now)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请求太频繁，稍后再试");
        }

        redisStore.set(KeyConstants.LAST_ACT_TIME_WDT_SYNC, now);

        LocalDateTime lastModifyAt = redisStore.get(KeyConstants.LAST_MODIFY_AT_WDT_SYNC, LocalDateTime.class);
        LambdaQueryWrapper<PhysicalGoodsEntity> wrapper = Wrappers.lambdaQuery();
        if (lastModifyAt != null) {
            wrapper.gt(PhysicalGoodsEntity::getModifiedAt, lastModifyAt);
        }
        wrapper.orderByAsc(PhysicalGoodsEntity::getModifiedAt);

        Page<PhysicalGoodsEntity> page = Page.of(1, maxPageSize);
        IPage<PhysicalGoodsEntity> physicalGoods = physicalGoodsService.page(page, wrapper);
        if (physicalGoods.getTotal() <= 0) {
            log.info("{} no update, stop it", jobName);
            return;
        }

        List<PhysicalGoodsEntity> toSyncList = physicalGoods.getRecords();

        WdtClient client = new WdtClient(sid, appkey, appsecret, baseUrl);
        Map<String, Object> apiGoodsInfo = new HashMap<>();
        Map<String, Object>[] goodsList = new Map[toSyncList.size()];

        for (int i = 0; i < toSyncList.size(); i++) {
            PhysicalGoodsEntity physicalGoodsEntity = toSyncList.get(i);
            goodsList[i] = new HashMap<>();
            goodsList[i].put("goods_id", physicalGoodsEntity.wdtSpuId());
            goodsList[i].put("spec_id", physicalGoodsEntity.wdtSkuId());
            goodsList[i].put("goods_no", physicalGoodsEntity.wdtSpuNo());
            goodsList[i].put("spec_no", physicalGoodsEntity.wdtSkuNo());
            goodsList[i].put("goods_name", physicalGoodsEntity.getName());
            goodsList[i].put("pic_url", physicalGoodsEntity.getCoverUrl());
            goodsList[i].put("status", "1");
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
            WdtGoodsSpecPushBasicResponse goodsSpecPushResponse = JsonUtils.fromJson(response,
                    WdtGoodsSpecPushBasicResponse.class);
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

    @Scheduled(initialDelay = initialDelay, fixedDelay = fixedDelay)
    public void goodsSpecPush() {
        boolean res = redisDistributedLocker.tryLock(jobName);
        if (!res) {
            log.info("{} Job already in progress", jobName);
            return;
        }
        try {
            log.info("{} Job start ...", jobName);
            if (!redisStore.exists(KeyConstants.FLAG_WDT_SYNC)) {
                redisStore.setWithTwoMinutesExpiration(KeyConstants.FLAG_WDT_SYNC, true);
                log.info("{} impl ...", jobName);
                goodsSpecPushImpl();
            } else {
                log.info("{} execute too frequently", jobName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            redisDistributedLocker.unlock(jobName);
        }
    }

}
