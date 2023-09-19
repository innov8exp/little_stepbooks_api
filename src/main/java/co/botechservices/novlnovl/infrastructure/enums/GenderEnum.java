package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum GenderEnum {
    MALE("MALE"), FEMALE("FEMALE"), OTHER("OTHER");

    @EnumValue
    @Getter
    private final String value;

    GenderEnum(String value) {
        this.value = value;
    }
}
