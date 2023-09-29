package com.stepbook.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum RecommendType {
    TOP_SEARCH("TOP_SEARCH"), TODAY("TODAY");

    @EnumValue
    private final String value;

    RecommendType(String value) {
        this.value = value;
    }
}
