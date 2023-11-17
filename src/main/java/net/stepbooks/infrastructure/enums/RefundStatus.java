package net.stepbooks.infrastructure.enums;

import lombok.Getter;

@Getter
public enum RefundStatus {
    PENDING,
    REFUNDING_WAIT_DELIVERY,
    REFUNDING_WAIT_SIGN,
    REFUNDING_WAIT_PAYMENT,
    REFUNDED,
    REFUND_FAILED,
    REFUND_CANCELLED;
}
