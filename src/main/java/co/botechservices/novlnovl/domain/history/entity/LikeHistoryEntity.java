package co.botechservices.novlnovl.domain.history.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_LIKE_HISTORY")
public class LikeHistoryEntity extends BaseEntity {
    private String bookId;
    private String userId;
}
