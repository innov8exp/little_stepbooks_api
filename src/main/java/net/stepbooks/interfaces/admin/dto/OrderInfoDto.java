package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.model.BaseDto;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoDto extends BaseDto {

    private String orderCode;
    private String userId;
    private String username;
    private String nickname;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    private String productId;
    private String skuCode;
    private BigDecimal totalAmount;
    private OrderState state;
    private ProductNature productNature;
    private PaymentStatus paymentStatus;

    @Deprecated
    private List<OrderProductDto> products;
    /**
     * 使用skus替代products
     */
    private List<OrderSkuDto> skus;

    private Delivery delivery;
    private List<OrderEventLog> eventLogs;
}
