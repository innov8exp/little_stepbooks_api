package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SmsType {
    VERIFICATION("VERIFICATION"), PROMOTION("PROMOTION");

    @EnumValue
    private final String value;

    SmsType(String value) {
        this.value = value;
    }
}
