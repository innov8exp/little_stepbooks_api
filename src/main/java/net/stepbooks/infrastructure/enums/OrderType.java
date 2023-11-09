package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum OrderType {
    PURCHASE("PURCHASE"), REFUND("REFUND");

    @EnumValue
    private final String value;

    OrderType(String value) {
        this.value = value;
    }
}
