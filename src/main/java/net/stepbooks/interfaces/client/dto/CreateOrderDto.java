package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class CreateOrderDto {

    private String userId;
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;
    private String skuCode;
    private int quantity;

}
