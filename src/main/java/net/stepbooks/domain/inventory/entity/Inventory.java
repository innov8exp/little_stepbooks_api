package net.stepbooks.domain.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_INVENTORY")
public class Inventory extends BaseEntity {

    private String productId;
    private Integer inventoryAmount;
}
