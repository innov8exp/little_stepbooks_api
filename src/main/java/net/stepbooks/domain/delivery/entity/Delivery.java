package net.stepbooks.domain.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.domain.delivery.enums.DeliveryMethod;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_DELIVERY")
public class Delivery extends BaseEntity {

    private String orderId;
    private String orderCode;
    private String userId;
    private DeliveryMethod deliveryMethod;
    private DeliveryStatus deliveryStatus;
    private String deliveryCode;
    private String deliveryCompany;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
}
