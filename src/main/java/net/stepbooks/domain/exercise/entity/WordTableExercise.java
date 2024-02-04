package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.util.JsonUtils;

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

    public static void main(String[] args) {
        WordTableExercise wordTableExercise = new WordTableExercise();
        wordTableExercise.setPoint(20);
        wordTableExercise.setAudioId("a_fake_audio_id");
        wordTableExercise.setAudioUrl("a_fake_autio_url");
        wordTableExercise.setImgId("a_fake_img_id");
        wordTableExercise.setImgUrl("a_fake_img_url");

        System.out.println(JsonUtils.toJson(wordTableExercise));
    }

}
