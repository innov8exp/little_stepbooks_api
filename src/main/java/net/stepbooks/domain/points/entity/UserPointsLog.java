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
@TableName("STEP_USER_POINTS_LOG")
public class UserPointsLog extends BaseEntity {

    private String userId;
    private PointsEventType eventType;
    private int pointsChange;

    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireAt;

}
