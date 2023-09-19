package co.botechservices.novlnovl.domain.dict.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_CATEGORY")
public class CategoryEntity extends BaseEntity {
    private String categoryName;
    private String description;
    private Integer sortIndex;
}
