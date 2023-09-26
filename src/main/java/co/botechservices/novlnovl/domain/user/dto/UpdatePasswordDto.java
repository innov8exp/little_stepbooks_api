package co.botechservices.novlnovl.domain.user.dto;

import co.botechservices.novlnovl.infrastructure.model.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


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
