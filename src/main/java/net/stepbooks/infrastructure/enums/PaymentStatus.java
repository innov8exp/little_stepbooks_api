package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PaymentStatus {
    UNPAID("UNPAID"), PAID("PAID");

    @EnumValue
    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
