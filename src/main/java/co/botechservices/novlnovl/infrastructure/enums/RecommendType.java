package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum RecommendType {
    TOP_SEARCH("TOP_SEARCH"), TODAY("TODAY");

    @EnumValue
    @Getter
    private final String value;

    RecommendType(String value) {
        this.value = value;
    }
}
