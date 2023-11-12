package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.order.entity.OrderProduct;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;

import java.util.List;

public interface OrderProductService extends IService<OrderProduct> {
    List<OrderProductDto> findByOrderId(String orderId);
}
