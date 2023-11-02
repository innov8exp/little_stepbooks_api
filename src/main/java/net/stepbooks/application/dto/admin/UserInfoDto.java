package net.stepbooks.application.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {
    private String username;
    private String email;
    private String nickname;
    private String phone;
    private String role;
    private String avatarImg;
    private String deviceId;
}
