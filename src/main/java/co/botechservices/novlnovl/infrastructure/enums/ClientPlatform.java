package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ClientPlatform {
    IOS("IOS"), ANDROID("ANDROID");

    @EnumValue
    private final String value;

    ClientPlatform(String value) {
        this.value = value;
    }
}
