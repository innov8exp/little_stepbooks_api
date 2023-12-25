package net.stepbooks.interfaces.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderDto {

    private List<SkuDto> skus;
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;

}
