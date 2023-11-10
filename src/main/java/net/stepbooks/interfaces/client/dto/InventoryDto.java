package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class InventoryDto {

    private String productId;
    private Integer inventoryAmount;
}
