package net.stepbooks.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmsSendResponse {

    private int respCode;
    private String respDesc;
    private Data data;

    @lombok.Data
    public static class Data {
        @JsonProperty("msgid")
        private Long msgId;
        private Integer contentCount;
        private Integer billingCount;
    }
}

