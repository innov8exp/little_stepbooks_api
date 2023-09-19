package co.botechservices.novlnovl.domain.admin.iam.entity;


import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("NOVL_ADMIN_USER")
public class AdminUserEntity extends BaseEntity {
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String avatarImg;
    private Boolean active;
    private String role;
}
