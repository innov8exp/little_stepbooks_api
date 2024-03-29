package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import net.stepbooks.domain.exercise.enums.ExerciseType;

/**
 * 练习内容
 */
@Data
public abstract class AbstractExercise {

    /**
     * 练习分数
     */
    private int point;

    /**
     * 练习类型
     */
    private ExerciseType type;
}
