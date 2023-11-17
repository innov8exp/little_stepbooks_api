package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import net.stepbooks.infrastructure.enums.RefundReason;

@Data
public class RefundRequestCreateDto {
    private String orderCode;
    private RefundReason refundReason;
    private String refundReasonDescription;
}
