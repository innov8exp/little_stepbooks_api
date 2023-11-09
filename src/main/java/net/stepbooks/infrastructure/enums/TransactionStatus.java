package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum TransactionStatus {
    SUCCESS("SUCCESS"), FAILED("FAILED");

    @EnumValue
    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }
}
