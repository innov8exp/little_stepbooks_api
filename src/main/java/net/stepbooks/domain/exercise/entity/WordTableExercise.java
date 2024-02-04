package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;

/**
 * 字表类型的练习
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WordTableExercise extends AbstractExercise {

    private String audioId;
    private String audioUrl;
    private String imgId;
    private String imgUrl;

    public WordTableExercise() {
        this.setType(ExerciseType.WORD_TABLE);
    }


}
