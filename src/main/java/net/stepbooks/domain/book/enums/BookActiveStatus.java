package net.stepbooks.domain.book.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum BookActiveStatus {

    ACTIVATED("ACTIVATED"), UNACTIVATED("UNACTIVATED");

    @EnumValue
    private final String value;

    BookActiveStatus(String value) {
        this.value = value;
    }
}
