package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class CreateOrderDto {

    private String userId;
    private String skuCode;
    private int quantity;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
}
