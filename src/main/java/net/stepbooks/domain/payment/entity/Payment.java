package net.stepbooks.domain.payment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PAYMENT")
public class Payment extends BaseEntity {

    private String orderId;
    private String orderCode;
    private String userId;
    private PaymentType paymentType;
    private PaymentMethod paymentMethod;
    private BigDecimal transactionAmount;
    private String vendorPaymentNo;
    private String receipt;
    private String transactionStatus;

}
