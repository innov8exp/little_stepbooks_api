package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.util.JsonUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoExercise extends AbstractExercise {

    private String videoId;
    private String videoUrl;

    public VideoExercise() {
        this.setType(ExerciseType.VIDEO);
    }

    public static void main(String[] args) {
        VideoExercise videoExercise = new VideoExercise();
        videoExercise.setPoint(20);
        videoExercise.setVideoId("a_fake_video_id");
        videoExercise.setVideoUrl("a_fake_video_url");

        System.out.println(JsonUtils.toJson(videoExercise));
    }

}
