package net.stepbooks.domain.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderState {
    INIT("INIT"),
    PLACED("PLACED"),
    PAID("PAID"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED"),
    RECEIVED("RECEIVED"),
    CANCELLED("CANCELLED"),
    CLOSED("CLOSED"),
    REFUNDED("REFUNDED"),
    FINISHED("FINISHED"),
    EXPIRED("EXPIRED"),
    DELETED("DELETED"),
    UNKNOWN("UNKNOWN");

    @EnumValue
    private final String value;

    OrderState(String value) {
        this.value = value;
    }
}
