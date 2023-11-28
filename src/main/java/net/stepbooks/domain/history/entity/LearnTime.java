package net.stepbooks.domain.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_LEARN_TIME")
public class LearnTime extends BaseEntity {

    private String userId;
    private String bookId;
    private String courseId;
    private Long duration;
    private Date learnDate;
}
