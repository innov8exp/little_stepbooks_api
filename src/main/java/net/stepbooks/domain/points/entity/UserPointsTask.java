package net.stepbooks.domain.points.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("STEP_USER_POINTS_TASK")
public class UserPointsTask extends BaseEntity {

    private String userId;
    private String taskId;

    private int points;

    private boolean completed;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate completedDate;

}
