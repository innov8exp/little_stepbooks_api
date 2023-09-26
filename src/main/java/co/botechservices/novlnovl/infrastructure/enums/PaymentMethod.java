package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PaymentMethod {
    APPLE_PAY("APPLE_PAY"), GOOGLE_PAY("GOOGLE_PAY");

    @EnumValue
    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}
