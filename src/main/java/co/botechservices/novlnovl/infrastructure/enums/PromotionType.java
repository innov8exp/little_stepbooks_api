package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum PromotionType {
    LIMIT_FREE("LIMIT_FREE"), LIMIT_DISCOUNT("LIMIT_DISCOUNT");

    @EnumValue
    @Getter
    private final String value;

    PromotionType(String value) {
        this.value = value;
    }
}
