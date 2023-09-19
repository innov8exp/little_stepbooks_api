package co.botechservices.novlnovl.domain.user.entity;

import co.botechservices.novlnovl.infrastructure.enums.AuthType;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_AUTH_HISTORY")
public class AuthHistoryEntity extends BaseEntity {
    private String username;
    private String email;
    private AuthType authType;
    private String googleId;
    private String facebookId;
}
