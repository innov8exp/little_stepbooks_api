package net.stepbooks.domain.user.entity;

import net.stepbooks.infrastructure.enums.AuthType;
import net.stepbooks.infrastructure.model.BaseEntity;
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
