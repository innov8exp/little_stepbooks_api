package net.stepbooks.interfaces.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminUserResetPasswordDto {
    @NotBlank(message = "phone number cannot be empty")
    private String phone;
}
