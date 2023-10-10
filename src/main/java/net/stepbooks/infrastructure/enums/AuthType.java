package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum AuthType {
    EMAIL("EMAIL"), GOOGLE("GOOGLE"), FACEBOOK("FACEBOOK"), GUEST("GUEST");

    @EnumValue
    private final String value;

    AuthType(String value) {
        this.value = value;
    }
}
