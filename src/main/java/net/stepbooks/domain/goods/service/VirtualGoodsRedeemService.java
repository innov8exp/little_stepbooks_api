package net.stepbooks.domain.goods.service;

import net.stepbooks.domain.order.entity.Order;

public interface VirtualGoodsRedeemService {

    /**
     * 当订单支付成功，如果RedeemCondition是PAYMENT_SUCCESS，那么立即兑现虚拟商品
     *
     * @param order
     */
    boolean redeemAfterOrderPaid(Order order);

    /**
     * 当订单签收成功，如果RedeemCondition是SIGN_SUCCESS，那么立即兑现虚拟商品
     *
     * @param order
     */
    boolean redeemAfterOrderSigned(Order order);

    /**
     * 管理后台强行兑换
     *
     * @param order
     */
    boolean redeemByAdmin(Order order);

}
