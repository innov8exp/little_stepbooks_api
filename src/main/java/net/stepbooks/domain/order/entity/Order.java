package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.OrderType;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_ORDER")
public class Order extends BaseEntity {

    private String orderCode;
    private String userId;
    private String recipientPhone;
    private OrderType orderType;
    private ProductNature productNature;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private Long paymentTimeoutDuration;
    private PaymentStatus paymentStatus;
    private OrderState state;
}
