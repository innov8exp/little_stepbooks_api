package net.stepbooks.domain.tag.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

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
