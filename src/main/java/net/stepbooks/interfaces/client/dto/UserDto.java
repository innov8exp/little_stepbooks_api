package net.stepbooks.interfaces.client.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.infrastructure.enums.ChildGender;
import net.stepbooks.infrastructure.enums.RoleEnum;


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
    private RoleEnum role;
    private String avatarImgId;
    private String avatarImgUrl;
    private String childAgeRange;
    private Float childMinAge;
    private Float childMaxAge;
    private ChildGender childGender;
    private String childClassificationId;
}
