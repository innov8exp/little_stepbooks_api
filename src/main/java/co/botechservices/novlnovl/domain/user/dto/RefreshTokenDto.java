package co.botechservices.novlnovl.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RefreshTokenDto {
    @NotBlank
    private String refreshToken;
}
