package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SalesPlatform {

    ANDROID("ANDROID"), IOS("IOS"), MINI_PROGRAM("MINI_PROGRAM");

    @EnumValue
    private final String value;

    SalesPlatform(String value) {
        this.value = value;
    }
}
