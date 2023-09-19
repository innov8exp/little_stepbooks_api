package co.botechservices.novlnovl.domain.admin.iam.dto;

import co.botechservices.novlnovl.infrastructure.model.ValidPassword;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
