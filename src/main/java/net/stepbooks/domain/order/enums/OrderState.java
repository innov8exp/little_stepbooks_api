package net.stepbooks.domain.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderState {
    INIT("INIT"),
    PLACED("PLACED"),
    PAID("PAID"),
    REFUND_APPLYING("REFUND_APPLYING"),
    REFUNDING("REFUNDING"),
    SHIPPED("SHIPPED"),
    CLOSED("CLOSED"),
    REFUNDED("REFUNDED"),
    FINISHED("FINISHED");

    @EnumValue
    private final String value;

    OrderState(String value) {
        this.value = value;
    }
}
