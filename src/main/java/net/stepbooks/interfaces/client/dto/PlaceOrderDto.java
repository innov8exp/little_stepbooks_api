package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class PlaceOrderDto {

    private String skuCode;
    private int quantity;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
}
