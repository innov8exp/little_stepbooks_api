package com.stepbook.interfaces.client.dto;

import com.stepbook.infrastructure.enums.AuthType;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthHistoryDto extends BaseDto {
    private String email;
    private AuthType authType;
    private String googleId;
    private String facebookId;
}
