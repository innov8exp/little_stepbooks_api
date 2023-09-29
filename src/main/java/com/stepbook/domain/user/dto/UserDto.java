package com.stepbook.domain.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    @Email
    private String email;
    private String nickname;
    private String phone;
    private String role;
    private String avatarImg;
    private String gender;
    private String deviceId;
    private String googleId;
    private String facebookId;
}
