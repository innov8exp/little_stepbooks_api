package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.enums.OrderStatus;
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

    private String orderNo;
    private String userId;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    private OrderType orderType;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private OrderStatus status;
}
