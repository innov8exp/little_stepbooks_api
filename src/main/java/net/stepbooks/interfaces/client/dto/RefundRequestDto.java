package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.product.entity.Product;

@Data
public class RefundRequestDto {
    private RefundRequest refundRequest;
    private Order order;
    private Product product;
}
