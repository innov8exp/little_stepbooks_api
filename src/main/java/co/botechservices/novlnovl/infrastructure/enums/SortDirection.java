package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum SortDirection {
    UP("UP"), DOWN("DOWN");

    @EnumValue
    @Getter
    private final String value;

    SortDirection(String value) {
        this.value = value;
    }
}
