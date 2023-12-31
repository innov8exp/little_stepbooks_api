package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE("MALE"), FEMALE("FEMALE"), OTHER("OTHER");

    @EnumValue
    private final String value;

    GenderEnum(String value) {
        this.value = value;
    }
}
