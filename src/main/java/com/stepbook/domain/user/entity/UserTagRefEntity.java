package com.stepbook.domain.user.entity;

import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

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
