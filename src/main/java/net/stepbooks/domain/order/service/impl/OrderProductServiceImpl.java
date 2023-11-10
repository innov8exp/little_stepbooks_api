package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.order.entity.OrderProduct;
import net.stepbooks.domain.order.mapper.OrderProductMapper;
import net.stepbooks.domain.order.service.OrderProductService;
import org.springframework.stereotype.Service;

@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct>
    implements OrderProductService {
}
