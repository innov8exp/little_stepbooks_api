package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;

/**
 * 伴读练习
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReadingExercise extends AbstractExercise {

    private String audioId;
    private String audioUrl;

    public ReadingExercise() {
        this.setType(ExerciseType.READING);
    }

}
