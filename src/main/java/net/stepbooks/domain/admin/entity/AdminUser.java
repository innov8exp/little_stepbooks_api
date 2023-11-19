package net.stepbooks.domain.admin.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("STEP_ADMIN_USER")
public class AdminUser extends BaseEntity {
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String avatarImgId;
    private String avatarImgUrl;
    private Boolean active;
    private String role;
}
