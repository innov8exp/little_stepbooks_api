package co.botechservices.novlnovl.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SocialAuthDto {
    private String googleId;
    private String facebookId;
    private String applicationId;
    @NotBlank
    private String deviceId;
    private String token;
    @NotBlank
    private String authType;
}
