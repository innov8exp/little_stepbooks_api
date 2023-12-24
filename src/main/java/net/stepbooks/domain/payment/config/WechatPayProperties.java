package net.stepbooks.domain.payment.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WechatPayProperties {
    /**
     * 微信公众号或者小程序等的appId
     */
    @Value("${wechat.appId}")
    private String appId;
    /**
     * 微信支付商户号
     */
    @Value("${wechat.merchant.id}")
    private String merchantId;
    /**
     * 商户证书路径
     */
    @Value("${wechat.merchant.cert-pem-path}")
    private String certPemPath;
    /**
     * 商户API私钥路径
     */
    @Value("${wechat.merchant.private-key-path}")
    private String privateKeyPath;
    /**
     * 商户APIv3密钥
     */
    @Value("${wechat.merchant.api-v3-key}")
    private String apiV3key;
    /**
     * 支付回调通知地址
     */
    @Value("${wechat.merchant.pay-notify-url}")
    private String payNotifyUrl;
    /**
     * 退款回调通知地址
     */
    @Value("${wechat.merchant.refund-notify-url}")
    private String refundNotifyUrl;
}
