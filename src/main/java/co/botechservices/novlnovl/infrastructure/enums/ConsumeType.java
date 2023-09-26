package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ConsumeType {
    SYSTEM_GIFT("SYSTEM_GIFT"), RECHARGE("RECHARGE");

    @EnumValue
    private final String value;

    ConsumeType(String value) {
        this.value = value;
    }
}
