package net.stepbooks.domain.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_LEARN_TIME")
public class LearnTime extends BaseEntity {

    private String userId;
    private Long duration;
    private LocalDateTime learnDateTime;

}
