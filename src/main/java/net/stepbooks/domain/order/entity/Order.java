package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.enums.RefundType;
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
    private ProductNature productNature;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal paymentAmount;
    private BigDecimal refundAmount;
    private Long paymentTimeoutDuration;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private RefundType refundType;
    private OrderState state;
    private Boolean redeemed;
}
