package net.stepbooks.application.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
