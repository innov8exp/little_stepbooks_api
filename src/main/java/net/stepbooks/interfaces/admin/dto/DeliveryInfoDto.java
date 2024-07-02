package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

@Data
public class DeliveryInfoDto {

    private String shipperUserId;

    private String logisticsType;
    private String logisticsName;
    private String logisticsNo;

}
