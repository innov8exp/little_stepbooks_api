package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ProductType {
    BOOK("BOOK"), COURSE("COURSE"), BOOK_COURSE_PACKAGE("BOOK_COURSE_PACKAGE"), COIN("COIN");

    @EnumValue
    private final String value;

    ProductType(String value) {
        this.value = value;
    }
}
