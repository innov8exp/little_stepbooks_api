package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum EmailType {
    REGISTER("REGISTER"), LINK("LINK"), FORGET("FORGET");

    @EnumValue
    @Getter
    private final String value;

    EmailType(String value) {
        this.value = value;
    }
}
