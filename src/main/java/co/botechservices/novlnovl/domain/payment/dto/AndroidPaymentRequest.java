package co.botechservices.novlnovl.domain.payment.dto;

import lombok.Data;

@Data
public class AndroidPaymentRequest {
    private String storeProductId;
    private String token;
}
