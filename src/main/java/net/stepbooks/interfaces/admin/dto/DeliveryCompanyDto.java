package net.stepbooks.interfaces.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryCompanyDto {
    private String code;
    private String name;
}
