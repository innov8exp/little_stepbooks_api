package net.stepbooks.domain.payment.service;

import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import net.stepbooks.domain.payment.vo.WechatPayPrePayRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundResponse;

public interface PaymentService {

    /**
     * 预支付
     * @param createOrderPay 订单请求体
     * @return PrepayWithRequestPaymentResponse 预支付响应
     */
    PrepayWithRequestPaymentResponse prepayWithRequestPayment(WechatPayPrePayRequest createOrderPay);

    /**
     * 查询状态
     *
     * @param outTradeNo 商户支付no
     * @return 状态信息
     */
    Transaction queryStatus(String outTradeNo);

    /**
     * 取消订单
     *
     * @param outTradeNo 微信支付订单号
     */
    void closeOrder(String outTradeNo);

    /**
     * 预支付回调
     * 官网地址：<a href="https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_1.shtml">...</a>
     *
     * @param requestParam 回调信息请求体
     * @param clazz 返回类
     * @param <T> 返回范型
     * @return 结果
     */
    <T> T prePayNotify(RequestParam requestParam, Class<T> clazz);

    /**
     * 退款
     *
     * @param refundOrder 退款请求
     * @return 退款返回信息
     */
    WechatPayRefundResponse refund(WechatPayRefundRequest refundOrder) throws Exception;

    /**
     * 退款回调
     *
     * @param requestParam 回调信息请求体
     * @param clazz 返回类
     * @param <T> 返回范型
     * @return 结果
     */
    <T> T refundNotify(RequestParam requestParam, Class<T> clazz) throws Exception;

}
