package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.util.JsonUtils;

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

    public static void main(String[] args) {
        ReadingExercise readingExercise = new ReadingExercise();
        readingExercise.setAudioId("audioId");
        readingExercise.setAudioUrl("audioUrl");
        readingExercise.setPoint(20);
        System.out.println(JsonUtils.toJson(readingExercise));
    }
}
