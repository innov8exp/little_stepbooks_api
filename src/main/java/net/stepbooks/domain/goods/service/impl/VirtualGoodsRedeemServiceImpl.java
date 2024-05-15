package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.service.MemberExpirationService;
import net.stepbooks.domain.goods.service.VirtualGoodsExpirationService;
import net.stepbooks.domain.goods.service.VirtualGoodsRedeemService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.product.entity.SkuVirtualGoods;
import net.stepbooks.domain.product.enums.RedeemCondition;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VirtualGoodsRedeemServiceImpl implements VirtualGoodsRedeemService {

    private final OrderSkuService orderSkuService;

    private final SkuVirtualGoodsService skuVirtualGoodsService;

    private final VirtualGoodsExpirationService virtualGoodsExpirationService;

    private final MemberExpirationService memberExpirationService;

    private final VirtualGoodsService virtualGoodsService;

    private void redeem(Order order, RedeemCondition redeemCondition) {
        List<OrderSkuDto> orderSkuDtos = orderSkuService.findOrderSkusByOrderId(order.getId());
        log.info("Redeem order={}, orderSkuDtos.size={}", order.getId(), orderSkuDtos.size());
        for (OrderSkuDto orderSkuDto : orderSkuDtos) {
            String skuId = orderSkuDto.getSkuId();
            int quantity = orderSkuDto.getQuantity();
            log.info("Redeem sku={}, quantity={}", skuId, quantity);

            LambdaQueryWrapper<SkuVirtualGoods> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(SkuVirtualGoods::getSkuId, skuId);

            //不传redeemCondition则返回全部，否则按照redeemCondition查询
            wrapper.eq(ObjectUtils.isNotEmpty(redeemCondition), SkuVirtualGoods::getRedeemCondition, redeemCondition);

            List<SkuVirtualGoods> skuVirtualGoodsList = skuVirtualGoodsService.list(wrapper);

            log.info("Redeem skuVirtualGoodsList.size={}", skuVirtualGoodsList.size());
            for (SkuVirtualGoods skuVirtualGoods : skuVirtualGoodsList) {
                String goodsId = skuVirtualGoods.getId();
                String categoryId = skuVirtualGoods.getCategoryId();
                if (AppConstants.VIRTUAL_CATEGORY_ID_MEMBER.equals(categoryId)) {
                    //兑换会员商品
                    VirtualGoodsEntity virtualGoodsEntity = virtualGoodsService.getById(goodsId);
                    int toAddMonth = virtualGoodsEntity.getToAddMonth();
                    toAddMonth = toAddMonth * quantity;
                    log.info("Redeem member userId={}, toAddMonth={}", order.getUserId(), toAddMonth);
                    memberExpirationService.redeem(order.getUserId(), toAddMonth);
                } else {
                    //兑换其他虚拟商品
                    int toAddMonth = AppConstants.TO_ADD_MONTH * quantity;
                    log.info("Redeem categoryId={}, goodsId={}, userId={}, toAddMonth={}",
                            categoryId, goodsId, order.getUserId(), toAddMonth);
                    virtualGoodsExpirationService.redeem(order.getUserId(), goodsId, categoryId, toAddMonth);
                }
            }
        }
    }

    @Override
    public void afterOrderPaid(Order order) {
        log.info("After order={} paid, redeem ...", order.getId());
        redeem(order, RedeemCondition.PAYMENT_SUCCESS);
    }

    @Override
    public void afterOrderSigned(Order order) {
        log.info("After order={} signed, redeem ...", order.getId());
        redeem(order, RedeemCondition.SIGN_SUCCESS);
    }

}
