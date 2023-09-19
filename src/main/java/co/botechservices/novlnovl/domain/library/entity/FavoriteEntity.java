package co.botechservices.novlnovl.domain.library.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_FAVORITE")
public class FavoriteEntity extends BaseEntity {
    private String bookId;
    private String userId;
    private Integer sortIndex;
}
