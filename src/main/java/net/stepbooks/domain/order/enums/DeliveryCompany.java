package net.stepbooks.domain.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum DeliveryCompany {
    SF_EXPRESS("SF_EXPRESS", "顺丰快递"),
    ZTO_EXPRESS("ZTO_EXPRESS", "中通快递"),
    YTO_EXPRESS("YTO_EXPRESS", "圆通快递"),
    STO_EXPRESS("STO_EXPRESS", "申通快递"),
    YUNDA_EXPRESS("YUNDA_EXPRESS", "韵达快递"),
    EMS("EMS", "EMS"),
    OTHER("OTHER", "其他");

    @EnumValue
    private final String key;
    private final String value;

    DeliveryCompany(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
