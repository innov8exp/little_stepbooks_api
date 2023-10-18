package net.stepbooks.domain.dict.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

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
