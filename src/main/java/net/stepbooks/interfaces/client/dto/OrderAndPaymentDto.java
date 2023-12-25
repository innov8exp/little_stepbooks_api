package net.stepbooks.interfaces.client.dto;

import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import lombok.Data;
import net.stepbooks.domain.order.entity.Order;

@Data
public class OrderAndPaymentDto {
    private Order order;
    private PrepayWithRequestPaymentResponse paymentResponse;
}
