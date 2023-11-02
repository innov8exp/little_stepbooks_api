package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SmsSignType {

    NORMAL("normal"), MD5("md5");

    @EnumValue
    private final String value;

    SmsSignType(String value) {
        this.value = value;
    }
}
