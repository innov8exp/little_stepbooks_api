package net.stepbooks.application.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class VerificationDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String emailType;

}
