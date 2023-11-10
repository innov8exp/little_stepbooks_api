package net.stepbooks.application.dto.client;

import lombok.Data;
import net.stepbooks.domain.order.enums.OrderState;

import java.math.BigDecimal;

@Data
public class OrderDto {

    private String orderCode;
    private String userId;
    private String productId;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderState state;
}
