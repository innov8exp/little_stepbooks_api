package co.botechservices.novlnovl.domain.order.entity;

import co.botechservices.novlnovl.infrastructure.enums.OrderStatus;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_ORDER")
public class OrderEntity extends BaseEntity {

    private String orderNo;
    private String productId;
    private String userId;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderStatus status;
}
