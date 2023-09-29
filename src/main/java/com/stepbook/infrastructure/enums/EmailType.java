package com.stepbook.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum EmailType {
    REGISTER("REGISTER"), LINK("LINK"), FORGET("FORGET");

    @EnumValue
    private final String value;

    EmailType(String value) {
        this.value = value;
    }
}
