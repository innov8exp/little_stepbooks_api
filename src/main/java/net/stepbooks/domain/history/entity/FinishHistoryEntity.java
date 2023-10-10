package net.stepbooks.domain.history.entity;

import net.stepbooks.infrastructure.model.BaseEntity;
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
