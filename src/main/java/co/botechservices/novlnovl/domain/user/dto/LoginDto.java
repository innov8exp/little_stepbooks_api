package co.botechservices.novlnovl.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
//    @ValidPassword
    private String password;
}
