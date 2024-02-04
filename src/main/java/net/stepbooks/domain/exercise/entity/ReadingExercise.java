package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
