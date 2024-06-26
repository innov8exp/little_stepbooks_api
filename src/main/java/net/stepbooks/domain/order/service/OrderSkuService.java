package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.order.entity.OrderSku;
import net.stepbooks.domain.product.entity.Sku;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;

import java.util.List;

public interface OrderSkuService extends IService<OrderSku> {

    List<Sku> findSkusByOrderId(String orderId);

    List<OrderSkuDto> findOrderSkusByOrderId(String orderId);
}
