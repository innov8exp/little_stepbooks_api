package net.stepbooks.application.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ResetPasswordDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String resetToken;
    @NotBlank
//    @ValidPassword
    private String newPassword;
}