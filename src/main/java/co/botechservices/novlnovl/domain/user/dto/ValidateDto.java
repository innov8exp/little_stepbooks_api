package co.botechservices.novlnovl.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ValidateDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String code;
    @NotBlank
    private String emailType;

}
