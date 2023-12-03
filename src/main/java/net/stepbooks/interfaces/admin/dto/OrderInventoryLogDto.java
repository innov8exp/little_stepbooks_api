package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.InventoryChangeType;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInventoryLogDto extends BaseDto {

    private String orderId;
    private String orderCode;
    private String productId;
    private String skuCode;
    private String skuName;
    private String inventoryId;
    private Integer quantity;
    private InventoryChangeType changeType;
}
