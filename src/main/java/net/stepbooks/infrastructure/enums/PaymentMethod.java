package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PaymentMethod {
    WECHAT_PAY("WECHAT_PAY"), ALI_PAY("ALI_PAY");

    @EnumValue
    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}
