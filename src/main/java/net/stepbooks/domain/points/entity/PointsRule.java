package net.stepbooks.domain.points.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("STEP_POINTS_RULE")
public class PointsRule extends BaseEntity {

    private PointsEventType eventType;

    /**
     * 当本规则生效的时候，其他几个规则失效
     */
    private String exclusiveTypes;
    private int points;
    private String reason;

    private boolean active;

    private String spus;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activityStartDay;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activityEndDay;

}
