package net.stepbooks.interfaces.admin.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class MInventoryDto extends BaseDto {

    private String skuCode;
    private String skuName;
    private Integer inventoryQuantity;

}
