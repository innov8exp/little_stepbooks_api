package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum BookStatus {
    ONLINE("ONLINE"), OFFLINE("OFFLINE");

    @EnumValue
    @Getter
    private final String value;

    BookStatus(String value) {
        this.value = value;
    }
}
