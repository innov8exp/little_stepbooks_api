package net.stepbooks.domain.order.entity;

import net.stepbooks.infrastructure.enums.OrderStatus;
import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_ORDER")
public class OrderEntity extends BaseEntity {

    private String orderNo;
    private String productId;
    private String userId;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderStatus status;
}
