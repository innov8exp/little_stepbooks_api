package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum MediaType {

    IMAGE("IMAGE"), AUDIO("AUDIO"), VIDEO("VIDEO");

    @EnumValue
    private final String value;

    MediaType(String value) {
        this.value = value;
    }
}
