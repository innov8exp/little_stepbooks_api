package co.botechservices.novlnovl.domain.admin.iam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

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
    private String avatarImg;
}
