package co.botechservices.novlnovl.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterDto {
    @NotBlank
    @Email
    private String email;
    //    @ValidPassword
    @NotBlank
    private String password;

    @NotBlank
    private String code;
    @NotBlank
    private String deviceId;
}
