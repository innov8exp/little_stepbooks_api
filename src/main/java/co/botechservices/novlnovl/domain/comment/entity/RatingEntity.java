package co.botechservices.novlnovl.domain.comment.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_RATING")
public class RatingEntity extends BaseEntity {
    private Integer rating;
    private String bookId;
    private String userId;
}
