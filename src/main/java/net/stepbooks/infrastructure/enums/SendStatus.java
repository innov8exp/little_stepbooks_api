package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SendStatus {

    SUCCESS("SUCCESS"), FAILED("FAILED");

    @EnumValue
    private final String value;

    SendStatus(String value) {
        this.value = value;
    }
}
