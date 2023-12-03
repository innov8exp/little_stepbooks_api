package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.enums.InventoryChangeType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_ORDER_INVENTORY_LOG")
public class OrderInventoryLog extends BaseEntity {

    private String orderId;
    private String orderCode;
    private String productId;
    private String skuCode;
    private String inventoryId;
    private Integer quantity;
    private InventoryChangeType changeType;
}
