package net.stepbooks.infrastructure.enums;

import lombok.Getter;

@Getter
public enum RefundReason {

    PRODUCT_QUALITY_PROBLEM("商品质量问题"),
    PRODUCT_NOT_RECEIVED("未收到货"),
    PRODUCT_NOT_MATCHED("商品与描述不符"),
    DO_NOT_WANT_ANYMORE("不想要了"),
    OTHER("其他");

    private final String description;

    RefundReason(String description) {
        this.description = description;
    }
}
