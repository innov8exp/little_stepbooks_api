package net.stepbooks.domain.payment.entity;

import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PAYMENT")
public class PaymentEntity extends BaseEntity {

    private String orderId;
    private String userId;
    private PaymentMethod paymentMethod;
    private BigDecimal transactionAmount;
    private String vendorPaymentNo;
    private String receipt;

}
