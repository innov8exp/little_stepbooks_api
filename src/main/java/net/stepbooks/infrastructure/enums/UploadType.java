package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum UploadType {
    NEW("NEW"), CONTINUE("CONTINUE");

    @EnumValue
    private final String value;

    UploadType(String value) {
        this.value = value;
    }
}
