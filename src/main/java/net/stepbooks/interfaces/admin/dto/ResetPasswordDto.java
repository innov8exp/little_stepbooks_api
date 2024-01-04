package net.stepbooks.interfaces.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import net.stepbooks.infrastructure.model.ValidPassword;


@Data
public class ResetPasswordDto {
    @NotBlank
    private String phone;
    @NotBlank
    private String verifyCode;
    @NotBlank
    @ValidPassword
    private String newPassword;
}
