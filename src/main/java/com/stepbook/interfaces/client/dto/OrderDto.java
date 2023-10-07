package com.stepbook.interfaces.client.dto;

import com.stepbook.infrastructure.enums.OrderStatus;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDto extends BaseDto {

    private String orderNo;
    private String userId;
    private String productId;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderStatus status;
}
