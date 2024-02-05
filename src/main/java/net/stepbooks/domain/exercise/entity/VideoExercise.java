package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoExercise extends AbstractExercise {
    private List<VideoContent> videoList;

    public VideoExercise() {
        this.setType(ExerciseType.VIDEO);
    }
}
