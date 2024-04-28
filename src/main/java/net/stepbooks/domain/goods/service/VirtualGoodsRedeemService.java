package net.stepbooks.domain.goods.service;

import net.stepbooks.domain.order.entity.Order;

public interface VirtualGoodsRedeemService {

    /**
     * 当订单支付成功，如果RedeemCondition是PAYMENT_SUCCESS，那么立即兑现虚拟商品
     *
     * @param order
     */
    void afterOrderPaid(Order order);

    /**
     * 当订单签收成功，如果RedeemCondition是SIGN_SUCCESS，那么立即兑现虚拟商品
     *
     * @param order
     */
    void afterOrderSigned(Order order);
}
