package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PromotionType {
    LIMIT_FREE("LIMIT_FREE"), LIMIT_DISCOUNT("LIMIT_DISCOUNT");

    @EnumValue
    private final String value;

    PromotionType(String value) {
        this.value = value;
    }
}
