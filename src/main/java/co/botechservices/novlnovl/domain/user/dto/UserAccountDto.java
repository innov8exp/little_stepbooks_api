package co.botechservices.novlnovl.domain.user.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccountDto extends BaseDto {
    private String userId;
    private BigDecimal coinBalance;
}
