package co.botechservices.novlnovl.domain.user.dto;

import co.botechservices.novlnovl.infrastructure.enums.AuthType;
import co.botechservices.novlnovl.infrastructure.model.BaseDto;
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
