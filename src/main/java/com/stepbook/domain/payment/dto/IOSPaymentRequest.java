package com.stepbook.domain.payment.dto;

import lombok.Data;

@Data
public class IOSPaymentRequest {
    private String storeProductId;
    private String receiptData;
}
