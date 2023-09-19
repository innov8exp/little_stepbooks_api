package co.botechservices.novlnovl.domain.admin.iam.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
//    @ValidPassword
    private String password;
}
