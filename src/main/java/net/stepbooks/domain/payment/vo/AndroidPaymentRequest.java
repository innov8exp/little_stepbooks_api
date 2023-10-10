package net.stepbooks.domain.payment.vo;

import lombok.Data;

@Data
public class AndroidPaymentRequest {
    private String storeProductId;
    private String token;
}
