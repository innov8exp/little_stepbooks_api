package co.botechservices.novlnovl.domain.user.entity;


import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("NOVL_USER")
public class UserEntity extends BaseEntity {
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String deviceId;
    private String avatarImg;
    private String gender;
    private Boolean active;
    private String role;
    private String googleId;
    private String facebookId;
}
