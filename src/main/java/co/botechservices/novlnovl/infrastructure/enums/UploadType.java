package co.botechservices.novlnovl.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum UploadType {
    NEW("NEW"), CONTINUE("CONTINUE");

    @EnumValue
    @Getter
    private final String value;

    UploadType(String value) {
        this.value = value;
    }
}
