package com.stepbook.interfaces.client.dto;

import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccountDto extends BaseDto {
    private String userId;
    private BigDecimal coinBalance;
}
