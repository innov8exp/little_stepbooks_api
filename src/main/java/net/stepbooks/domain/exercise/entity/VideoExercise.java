package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoExercise extends AbstractExercise {

    private String videoId;
    private String videoUrl;

    public VideoExercise() {
        this.setType(ExerciseType.VIDEO);
    }

}
