package net.stepbooks.domain.user.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.enums.ChildGender;
import net.stepbooks.infrastructure.enums.GenderEnum;
import net.stepbooks.infrastructure.enums.RoleEnum;
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
    private Boolean active;
    private RoleEnum role;
    private String googleId;
    private String facebookId;
    private String wechatId;
    private String alipayId;
    private ChildGender childGender;
    private String childClassificationId;
    private Float childMinAge;
    private Float childMaxAge;
    private String openId;
    private String unionId;
}
