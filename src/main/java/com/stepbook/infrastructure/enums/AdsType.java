package com.stepbook.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum AdsType {
    RECOMMEND("RECOMMEND"), CAROUSEL("CAROUSEL");

    @EnumValue
    @Getter
    private final String value;

    AdsType(String value) {
        this.value = value;
    }
}
