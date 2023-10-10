package net.stepbooks.domain.bookshelf.entity;

import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_FAVORITE")
public class FavoriteEntity extends BaseEntity {
    private String bookId;
    private String userId;
    private Integer sortIndex;
}
