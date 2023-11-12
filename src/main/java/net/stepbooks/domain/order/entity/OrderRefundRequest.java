package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.order.enums.RefundStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_ORDER_REFUND_REQUEST")
public class OrderRefundRequest extends BaseEntity {

    private String orderId;
    private String orderCode;
    private String userId;
    private String refundAmount;
    private String refundReason;
    private RefundStatus refundStatus;

}
