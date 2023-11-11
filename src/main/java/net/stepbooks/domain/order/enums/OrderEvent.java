package net.stepbooks.domain.order.enums;

public enum OrderEvent {
    PLACE_SUCCESS,
    PAY_SUCCESS,
    REFUND_APPLICATION,
    SHIP_SUCCESS,
    SHIPPED_TO_REFUND,
    RECEIVED_SUCCESS,

    USER_MANUAL_CANCEL,
    PAYMENT_TIMEOUT,

    ADMIN_MANUAL_CLOSE,
    REFUND_SUCCESS,
}
