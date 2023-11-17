package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class CreateOrderDto {

    private String userId;
    private String recipientName;
    private String recipientPhone;
    private String recipientLocation;
    private String recipientAddress;
    private String skuCode;
    private int quantity;

}
