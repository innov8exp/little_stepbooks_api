package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MInventoryDto {

    private String id;
    private String productId;
    private String skuCode;
    private String skuName;
    private Integer inventoryQuantity;

}
