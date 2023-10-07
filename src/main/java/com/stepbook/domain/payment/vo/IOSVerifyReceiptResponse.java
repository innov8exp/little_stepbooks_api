package com.stepbook.domain.payment.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class IOSVerifyReceiptResponse {

    private String environment;
    @JsonProperty("is-retryable")
    private Boolean isRetryable;
    @JsonProperty("latest_receipt")
    private String latestReceipt;
    @JsonProperty("latest_receipt_info")
    private List<Map<String, Object>> latestReceiptInfo;
    @JsonProperty("pending_renewal_info")
    private Map<String, Object> pendingRenewalInfo;
    private Map<String, Object> receipt;
    private Integer status;
}
