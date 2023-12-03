package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

@Data
public class DeliveryDetailDto {
    private String deliveryCode;
    private String deliveryCompany;
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;
}
