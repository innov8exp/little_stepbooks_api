package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.order.enums.RequestStatus;
import net.stepbooks.infrastructure.enums.RefundReason;
import net.stepbooks.infrastructure.enums.RefundStatus;
import net.stepbooks.infrastructure.enums.RefundType;
import net.stepbooks.infrastructure.model.BaseDto;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class RefundRequestDto extends BaseDto {
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
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;
    private String username;
    private String nickname;
    private String phone;
}
