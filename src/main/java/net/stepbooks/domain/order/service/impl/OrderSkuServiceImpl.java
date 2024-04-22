package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderSku;
import net.stepbooks.domain.order.mapper.OrderSkuMapper;
import net.stepbooks.domain.order.service.OrderSkuService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSkuServiceImpl extends ServiceImpl<OrderSkuMapper, OrderSku>
        implements OrderSkuService {
}
