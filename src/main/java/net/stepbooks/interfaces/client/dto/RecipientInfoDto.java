package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class RecipientInfoDto {
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;
}
