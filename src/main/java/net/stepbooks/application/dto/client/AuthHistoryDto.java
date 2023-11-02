package net.stepbooks.application.dto.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.AuthType;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthHistoryDto extends BaseDto {
    private String email;
    private AuthType authType;
    private String googleId;
    private String facebookId;
}
