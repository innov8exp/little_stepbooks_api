package net.stepbooks.application.dto.client;

import lombok.Data;

@Data
public class InventoryDto {

    private String productId;
    private Integer inventoryAmount;
}
