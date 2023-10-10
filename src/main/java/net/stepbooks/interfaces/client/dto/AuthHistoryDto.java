package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.enums.AuthType;
import net.stepbooks.infrastructure.model.BaseDto;
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
