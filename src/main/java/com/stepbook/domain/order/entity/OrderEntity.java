package com.stepbook.domain.order.entity;

import com.stepbook.infrastructure.enums.OrderStatus;
import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_ORDER")
public class OrderEntity extends BaseEntity {

    private String orderNo;
    private String productId;
    private String userId;
    private BigDecimal transactionAmount;
    private BigDecimal coinAmount;
    private OrderStatus status;
}
