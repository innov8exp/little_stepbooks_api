package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ProductObjectType {
    PHYSICAL("PHYSICAL"), VIRTUAL("VIRTUAL");

    @EnumValue
    private final String value;

    ProductObjectType(String value) {
        this.value = value;
    }
}
