package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum BookStatus {
    ONLINE("ONLINE"), OFFLINE("OFFLINE");

    @EnumValue
    private final String value;

    BookStatus(String value) {
        this.value = value;
    }
}
