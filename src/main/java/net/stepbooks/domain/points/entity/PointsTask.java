package net.stepbooks.domain.points.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.domain.points.enums.PointsTaskType;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("STEP_POINTS_TASK")
public class PointsTask extends BaseEntity {

    private String name;
    private PointsTaskType type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String successHint;

    private Integer points;

    private String actionUrl;

    private boolean active;

}
