package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ChargeType {
    FREE("FREE"), FULL_CHARGE("FULL_CHARGE"), PART_CHARGE("PART_CHARGE");

    @EnumValue
    private final String value;

    ChargeType(String value) {
        this.value = value;
    }
}
