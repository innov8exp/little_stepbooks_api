package net.stepbooks.domain.payment.vo;

import lombok.Data;

@Data
public class WechatPayPreNotifyRequest {
    private String serial;
    private String nonce;
    private String signatureType;
    private String signature;
    private String timestamp;
    private String body;
}
