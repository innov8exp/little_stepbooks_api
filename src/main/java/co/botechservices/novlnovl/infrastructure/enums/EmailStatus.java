package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum EmailStatus {

    SUCCESS("SUCCESS"), FAILED("FAILED");

    @EnumValue
    private final String value;

    EmailStatus(String value) {
        this.value = value;
    }
}
