package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum EmailStatus {

    SUCCESS("SUCCESS"), FAILED("FAILED");

    @EnumValue
    @Getter
    private final String value;

    EmailStatus(String value) {
        this.value = value;
    }
}
