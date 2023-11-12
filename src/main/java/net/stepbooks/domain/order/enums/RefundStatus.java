package net.stepbooks.domain.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum RefundStatus {
    PENDING("PENDING"), APPROVED("APPROVED"), REJECTED("REJECTED");

    @EnumValue
    private final String value;

    RefundStatus(String value) {
        this.value = value;
    }
}
