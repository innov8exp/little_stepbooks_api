package com.stepbook.interfaces.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


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
