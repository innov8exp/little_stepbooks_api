package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.order.enums.RequestStatus;
import net.stepbooks.infrastructure.enums.RefundReason;
import net.stepbooks.infrastructure.enums.RefundStatus;
import net.stepbooks.infrastructure.enums.RefundType;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_REFUND_REQUEST")
public class RefundRequest extends BaseEntity {
    private String orderId;
    private String orderCode;
    private String userId;
    private BigDecimal refundAmount;
    private RefundReason refundReason;
    private String refundReasonDescription;
    private RefundType refundType;
    private String rejectReason;
    private RequestStatus requestStatus;
    private RefundStatus refundStatus;
    private String deliveryCode;
    private String deliveryCompany;
    private DeliveryStatus deliveryStatus;
    private String recipientName;
    private String recipientPhone;
    private String recipientLocation;
    private String recipientAddress;
}
