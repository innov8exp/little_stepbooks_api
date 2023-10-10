package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccountDto extends BaseDto {
    private String userId;
    private BigDecimal coinBalance;
}
