package com.stepbook.domain.user.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserAccountUpdateDto {
    private BigDecimal coin;
}
