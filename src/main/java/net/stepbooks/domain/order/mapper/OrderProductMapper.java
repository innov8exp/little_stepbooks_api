package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.order.entity.OrderProduct;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;

import java.util.List;

public interface OrderProductMapper extends BaseMapper<OrderProduct> {
    OrderProductDto findProductDetailsByOrderId(String orderId);

    List<OrderProductDto> findProductsDetailsByOrderIds(List<String> orderIds);
}
