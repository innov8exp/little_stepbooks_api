package net.stepbooks.interfaces.admin.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
    private String username;
    @Email
    private String email;
    private String nickname;
    private String phone;
    private String role;
    private String avatarImgId;
    private String avatarImgUrl;
}
