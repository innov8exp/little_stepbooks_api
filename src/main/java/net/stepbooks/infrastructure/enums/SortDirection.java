package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SortDirection {
    UP("UP"), DOWN("DOWN");

    @EnumValue
    private final String value;

    SortDirection(String value) {
        this.value = value;
    }
}
