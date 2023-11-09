package net.stepbooks.application.dto.client;

import lombok.Data;

@Data
public class CreateOrderDto {

    private String userId;
    private String skuNo;
    private int quantity;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
}
