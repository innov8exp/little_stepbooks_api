package net.stepbooks.domain.payment.vo;

import lombok.Data;

@Data
public class IOSPaymentRequest {
    private String storeProductId;
    private String receiptData;
}
