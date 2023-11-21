package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderProduct;
import net.stepbooks.domain.order.mapper.OrderProductMapper;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct>
    implements OrderProductService {

    private final OrderProductMapper orderProductMapper;

    @Override
    public OrderProductDto findByOrderId(String orderId) {
        return orderProductMapper.findProductDetailsByOrderId(orderId);
    }

    @Override
    public List<OrderProductDto> findByOrderIds(List<String> orderIds) {
        return orderProductMapper.findProductsDetailsByOrderIds(orderIds);
    }
}
