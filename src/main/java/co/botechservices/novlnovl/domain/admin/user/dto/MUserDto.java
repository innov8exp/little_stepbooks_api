package co.botechservices.novlnovl.domain.admin.user.dto;


import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MUserDto extends BaseDto {
    private String email;
    private String username;
    private String nickname;
    private String phone;
    private String role;
    private String avatarImg;
    private String gender;
    private String deviceId;
    private Boolean active;
    private String googleId;
    private String facebookId;
}
