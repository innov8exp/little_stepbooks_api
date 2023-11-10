package net.stepbooks.interfaces.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import net.stepbooks.infrastructure.model.ValidPassword;


@Data
public class ResetPasswordDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String resetToken;
    @NotBlank
    @ValidPassword
    private String newPassword;
}
