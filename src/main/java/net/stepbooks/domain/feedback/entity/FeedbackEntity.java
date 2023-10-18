package net.stepbooks.domain.feedback.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_FEEDBACK")
public class FeedbackEntity extends BaseEntity {
    private String userId;
    private String content;
}
