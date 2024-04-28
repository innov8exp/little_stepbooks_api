package net.stepbooks.domain.goods.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.service.VirtualGoodsExpirationService;
import net.stepbooks.domain.goods.service.VirtualGoodsRedeemService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.product.enums.RedeemCondition;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VirtualGoodsRedeemServiceImpl implements VirtualGoodsRedeemService {

    private final OrderSkuService orderSkuService;

    private final SkuVirtualGoodsService skuVirtualGoodsService;

    private final VirtualGoodsExpirationService virtualGoodsExpirationService;

    private void redeem(Order order, RedeemCondition redeemCondition) {
        List<OrderSkuDto> orderSkuDtos = orderSkuService.findOrderSkusByOrderId(order.getId());
        for (OrderSkuDto orderSkuDto : orderSkuDtos) {
            String skuId = orderSkuDto.getSkuId();
            int quantity = orderSkuDto.getQuantity();
            List<VirtualGoodsDto> virtualGoodsDtos = skuVirtualGoodsService.
                    getVirtualGoodsListBySkuId(skuId, redeemCondition);
            for (VirtualGoodsDto virtualGoodsDto : virtualGoodsDtos) {
                String goodsId = virtualGoodsDto.getId();
                String categoryId = virtualGoodsDto.getCategoryId();
                int toAddMonth = virtualGoodsDto.getToAddMonth();
                toAddMonth = toAddMonth * quantity;
                if (AppConstants.VIRTUAL_CATEGORY_ID_MEMBER.equals(categoryId)) {
                    log.info("redeem membership");
                } else {
                    //兑换其他虚拟商品
                    virtualGoodsExpirationService.redeem(order.getUserId(), goodsId, categoryId, toAddMonth);
                }
            }
        }
    }

    @Override
    public void afterOrderPaid(Order order) {
        redeem(order, RedeemCondition.PAYMENT_SUCCESS);
    }

    @Override
    public void afterOrderSigned(Order order) {
        redeem(order, RedeemCondition.SIGN_SUCCESS);
    }

}
