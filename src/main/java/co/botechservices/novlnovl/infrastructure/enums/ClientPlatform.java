package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum ClientPlatform {
    IOS("IOS"), ANDROID("ANDROID");

    @EnumValue
    @Getter
    private final String value;

    ClientPlatform(String value) {
        this.value = value;
    }
}
