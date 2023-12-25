package net.stepbooks.domain.payment.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WechatPayPrePayRequest {
//    /**
//     * 订单编号
//     */
//    private String orderCode;

    /**
     * 商户支付no 和微信交互 查询订单使用（outTradeNo）
     */
    private String outTradeNo;

    /**
     * 用户openid
     */
    private String openId;

    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 支付内容
     */
    private String payContent;
}
