package net.stepbooks.domain.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_USER_TAG_REF")
public class UserTagRefEntity extends BaseEntity {
    private String userId;
    private String tagId;
}
