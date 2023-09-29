package com.stepbook.domain.history.entity;

import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_FINISH_HISTORY")
public class FinishHistoryEntity extends BaseEntity {

    private String userId;
    private String bookId;
}
