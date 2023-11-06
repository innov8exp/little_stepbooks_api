package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum AdsType {
    RECOMMEND("RECOMMEND"), CAROUSEL("CAROUSEL");

    @EnumValue
    private final String value;

    AdsType(String value) {
        this.value = value;
    }
}