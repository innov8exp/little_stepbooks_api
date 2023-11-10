package net.stepbooks.interfaces.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryQueryDto {

    private String skuCode;

}
