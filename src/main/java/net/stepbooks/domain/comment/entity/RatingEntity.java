package net.stepbooks.domain.comment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_RATING")
public class RatingEntity extends BaseEntity {
    private Integer rating;
    private String bookId;
    private String userId;
}
