package net.stepbooks.domain.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_EXERCISE_HISTORY")
public class ExerciseHistory extends BaseEntity {
    private String exerciseId;
    private String courseId;
    private String userId;
    private Integer score;
}
