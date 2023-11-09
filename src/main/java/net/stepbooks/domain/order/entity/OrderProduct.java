package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_ORDER_PRODUCT")
public class OrderProduct extends BaseEntity {

        private String orderId;
        private String productId;
        private int quantity;
}
