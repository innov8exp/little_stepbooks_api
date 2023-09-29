package com.stepbook.domain.admin.iam.dto;

import com.stepbook.infrastructure.model.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UpdatePasswordDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    @ValidPassword
    private String oldPassword;
    @NotBlank
    @ValidPassword
    private String newPassword;
}
