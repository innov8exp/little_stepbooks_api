package com.stepbook.domain.user.entity;

import com.stepbook.infrastructure.enums.AuthType;
import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_AUTH_HISTORY")
public class AuthHistoryEntity extends BaseEntity {
    private String username;
    private String email;
    private AuthType authType;
    private String googleId;
    private String facebookId;
}
