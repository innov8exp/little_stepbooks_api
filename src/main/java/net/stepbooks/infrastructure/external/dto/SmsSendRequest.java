package net.stepbooks.infrastructure.external.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmsSendRequest {
    private String account;
    private Long timestamp;
    private String nonce;
    private String signType;
    private String signature;
    private Param param;

    @Data
    @Builder
    public static class Param {
        private String mobile;
        private String content;
    }
}


