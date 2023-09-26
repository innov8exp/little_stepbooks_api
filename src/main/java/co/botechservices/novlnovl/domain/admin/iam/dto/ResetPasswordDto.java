package co.botechservices.novlnovl.domain.admin.iam.dto;

import co.botechservices.novlnovl.infrastructure.model.ValidPassword;
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
    @ValidPassword
    private String newPassword;
}
