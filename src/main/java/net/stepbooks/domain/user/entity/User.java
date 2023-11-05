package net.stepbooks.domain.user.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("STEP_USER")
public class User extends BaseEntity {
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String deviceId;
    private String avatarImgId;
    private String avatarImgUrl;
    private String gender;
    private Boolean active;
    private String role;
    private String googleId;
    private String facebookId;
    private String wechatId;
    private String alipayId;
    private String openId;
    private String childClassificationId;
    private String childMinAge;
    private String childMaxAge;
}
