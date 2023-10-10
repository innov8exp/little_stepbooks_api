package net.stepbooks.domain.dict.entity;

import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_TAG")
public class TagEntity extends BaseEntity {
    private String tagName;
    private String description;
}
