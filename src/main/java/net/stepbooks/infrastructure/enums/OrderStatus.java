package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderStatus {
    UNPAID("UNPAID"), PAID("PAID");

    @EnumValue
    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
