package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;

import java.util.List;

/**
 * 伴读练习
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReadingExercise extends AbstractExercise {

    private List<ReadingContent> readingList;

    public ReadingExercise() {
        this.setType(ExerciseType.READING);
    }

}
