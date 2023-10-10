package net.stepbooks.domain.history.entity;

import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_LIKE_HISTORY")
public class LikeHistoryEntity extends BaseEntity {
    private String bookId;
    private String userId;
}
