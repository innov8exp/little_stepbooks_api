package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 0: NONE, 1: MINI_PROGRAM, 2: APP, 3: MINI_PROGRAM + APP
 */
@Getter
public enum SalesPlatform {
    NONE(0),
    MINI_PROGRAM(1),
    APP(2);

    @EnumValue
    private final int mask;

    SalesPlatform(int mask) {
        this.mask = mask;
    }

    // 解析数据库中的值并返回所选平台组
    public static SalesPlatform[] parseSalesPlatforms(int value) {
        SalesPlatform[] selectedSalesPlatforms = new SalesPlatform[Integer.bitCount(value)];
        int index = 0;
        for (SalesPlatform salesPlatform : SalesPlatform.values()) {
            if ((value & salesPlatform.getMask()) != 0) {
                selectedSalesPlatforms[index++] = salesPlatform;
            }
        }
        return selectedSalesPlatforms;
    }

    // 解析所选平台组并返回数据库中的值
    public static int parseSalesPlatforms(SalesPlatform[] salesPlatforms) {
        int value = 0;
        for (SalesPlatform salesPlatform : salesPlatforms) {
            value |= salesPlatform.getMask();
        }
        return value;
    }
}
