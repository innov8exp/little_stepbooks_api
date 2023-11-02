package net.stepbooks.application.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class GuestAuthDto {
    @NotBlank
    private String deviceId;
}
