package co.botechservices.novlnovl.domain.admin.order.dto;

import co.botechservices.novlnovl.infrastructure.enums.OrderStatus;
import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
