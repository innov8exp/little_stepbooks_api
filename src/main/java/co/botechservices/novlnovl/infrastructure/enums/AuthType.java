package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum AuthType {
    EMAIL("EMAIL"), GOOGLE("GOOGLE"), FACEBOOK("FACEBOOK"), GUEST("GUEST");

    @EnumValue
    @Getter
    private final String value;

    AuthType(String value) {
        this.value = value;
    }
}
