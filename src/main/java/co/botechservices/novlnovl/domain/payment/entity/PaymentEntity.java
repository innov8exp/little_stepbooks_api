package co.botechservices.novlnovl.domain.payment.entity;

import co.botechservices.novlnovl.infrastructure.enums.PaymentMethod;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_PAYMENT")
public class PaymentEntity extends BaseEntity {

    private String orderId;
    private String userId;
    private PaymentMethod paymentMethod;
    private BigDecimal transactionAmount;
    private String vendorPaymentNo;
    private String receipt;

}
