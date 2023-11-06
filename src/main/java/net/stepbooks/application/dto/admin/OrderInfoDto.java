package net.stepbooks.application.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.OrderStatus;
import net.stepbooks.infrastructure.model.BaseDto;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoDto extends BaseDto {

    private String orderNo;
    private String userId;
    private String username;
    private String nickname;
    private String productId;
    private String productNo;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderStatus status;
}