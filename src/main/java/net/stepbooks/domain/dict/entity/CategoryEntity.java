package net.stepbooks.domain.dict.entity;

import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_CATEGORY")
public class CategoryEntity extends BaseEntity {
    private String categoryName;
    private String description;
    private Integer sortIndex;
}
