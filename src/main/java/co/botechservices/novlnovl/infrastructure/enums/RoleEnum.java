package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum RoleEnum {
    ADMIN("ADMIN"), VIP_USER("VIP_USER"), NORMAL_USER("NORMAL_USER"), GUEST("GUEST");

    @EnumValue
    @Getter
    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }
}
