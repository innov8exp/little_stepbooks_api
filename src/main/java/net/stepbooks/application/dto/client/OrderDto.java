package net.stepbooks.application.dto.client;

import lombok.Data;
import net.stepbooks.infrastructure.enums.OrderStatus;

import java.math.BigDecimal;

@Data
public class OrderDto {

    private String orderNo;
    private String userId;
    private String productId;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderStatus status;
}
