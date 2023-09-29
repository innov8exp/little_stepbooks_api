package com.stepbook.domain.feedback.entity;

import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_FEEDBACK")
public class FeedbackEntity extends BaseEntity {
    private String userId;
    private String content;
}
