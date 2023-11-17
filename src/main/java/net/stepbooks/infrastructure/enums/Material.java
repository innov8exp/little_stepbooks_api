package net.stepbooks.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 0: NONE, 1: AUDIO, 2: COURSE, 4: EXERCISE,
 * 3: AUDIO + COURSE, 5: AUDIO + EXERCISE, 6: COURSE + EXERCISE, 7: AUDIO + COURSE + EXERCISE
 */
@Getter
public enum Material {

    NONE(0),
    AUDIO(1),
    COURSE(2),
    EXERCISE(4);

    @EnumValue
    private final int mask;

    Material(int mask) {
        this.mask = mask;
    }

    // 解析数据库中的值并返回所选材料数组
    public static Material[] parseMaterials(int value) {
        Material[] selectedMaterials = new Material[Integer.bitCount(value)];
        int index = 0;
        for (Material material : Material.values()) {
            if ((value & material.getMask()) != 0) {
                selectedMaterials[index++] = material;
            }
        }
        return selectedMaterials;
    }

    // 解析所选材料数组并返回数据库中的值
    public static int parseMaterials(Material[] materials) {
        int value = 0;
        for (Material material : materials) {
            value |= material.getMask();
        }
        return value;
    }

}
