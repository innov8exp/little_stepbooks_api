package net.stepbooks.domain.order.enums;

public enum OrderEvent {
    PLACE_SUCCESS,
    PAYMENT_SUCCESS,
    PAYMENT_TIMEOUT,

    REFUND_REQUEST,
    REFUND_APPROVE,
    REFUND_REJECT,
    REFUND_SUCCESS,

    SHIP_SUCCESS,

    SIGN_SUCCESS,

    USER_MANUAL_CANCEL,
    ADMIN_MANUAL_CLOSE,
}
