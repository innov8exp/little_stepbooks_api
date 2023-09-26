package co.botechservices.novlnovl.domain.admin.iam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RefreshTokenDto {
    @NotBlank
    private String refreshToken;
}
