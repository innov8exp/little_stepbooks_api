package com.stepbook.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class GuestAuthDto {
    @NotBlank
    private String deviceId;
}
