package net.stepbooks.domain.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.AuthType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_AUTH_HISTORY")
public class AuthHistoryEntity extends BaseEntity {
    private String username;
    private String email;
    private AuthType authType;
    private String phone;
    private String wechatId;
    private String googleId;
    private String facebookId;
}
