package net.stepbooks.interfaces.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {

    private String userId;
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;
    private List<SkuDto> skus;

}
