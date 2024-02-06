package net.stepbooks.domain.exercise.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.exercise.enums.ExerciseType;

import java.util.List;

/**
 * 拖拽型练习。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DragExercise extends AbstractExercise {

    private String imgId;
    private String imgUrl;
    private String audioId;
    private String audioUrl;

    /**
     * 答案图
     */
    private List<DragImage> dataList;

    public DragExercise() {
        this.setType(ExerciseType.DRAG);
    }
}
