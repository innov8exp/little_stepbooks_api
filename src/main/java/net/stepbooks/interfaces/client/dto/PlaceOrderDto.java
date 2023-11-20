package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class PlaceOrderDto {

    private String skuCode;
    private int quantity;
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;
}
