package net.stepbooks.domain.payment.vo;

import lombok.Data;

@Data
public class AndroidVerifyReceiptRequest {

    private String productId;
    private String token;
}
