package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;

import java.util.List;

/**
 * 字表类型的练习
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WordTableExercise extends AbstractExercise {

    @Deprecated
    private List<AudioContent> audioList;

    private List<VideoContent> videoList;

    public WordTableExercise() {
        this.setType(ExerciseType.WORD_TABLE);
    }

}
