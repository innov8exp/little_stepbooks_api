package net.stepbooks.application.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import net.stepbooks.infrastructure.model.ValidPassword;


@Data
public class UpdatePasswordDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    @ValidPassword
    private String oldPassword;
    @NotBlank
    @ValidPassword
    private String newPassword;
}
