package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PackageType {
    SINGLE("SINGLE"), PACKAGE("PACKAGE");

    @EnumValue
    private final String value;

    PackageType(String value) {
        this.value = value;
    }
}
