package co.botechservices.novlnovl.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
