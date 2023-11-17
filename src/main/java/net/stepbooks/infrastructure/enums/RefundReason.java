package net.stepbooks.infrastructure.enums;

import lombok.Getter;

@Getter
public enum RefundReason {

    PRODUCT_QUALITY_PROBLEM,
    PRODUCT_NOT_RECEIVED,
    PRODUCT_NOT_MATCHED,
    OTHER;
}
