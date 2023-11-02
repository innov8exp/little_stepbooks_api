package net.stepbooks.application.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RefreshTokenDto {
    @NotBlank
    private String refreshToken;
}
