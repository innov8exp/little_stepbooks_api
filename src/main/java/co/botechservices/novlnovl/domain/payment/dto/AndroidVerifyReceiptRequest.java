package co.botechservices.novlnovl.domain.payment.dto;

import lombok.Data;

@Data
public class AndroidVerifyReceiptRequest {

    private String productId;
    private String token;
}
