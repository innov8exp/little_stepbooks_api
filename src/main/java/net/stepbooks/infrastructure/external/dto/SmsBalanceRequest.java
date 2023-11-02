package net.stepbooks.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsBalanceRequest {
    private String account;
    private Long timestamp;
    private String nonce;
    private String signType;
    private String signature;
}
