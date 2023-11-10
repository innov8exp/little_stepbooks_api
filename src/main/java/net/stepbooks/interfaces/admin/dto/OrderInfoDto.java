package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.infrastructure.model.BaseDto;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoDto extends BaseDto {

    private String orderCode;
    private String userId;
    private String username;
    private String nickname;
    private String productId;
    private String productNo;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderState state;
}
