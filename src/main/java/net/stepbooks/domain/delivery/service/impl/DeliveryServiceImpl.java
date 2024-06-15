package net.stepbooks.domain.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.mapper.DeliveryMapper;
import net.stepbooks.domain.delivery.service.DeliveryService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {

    @Override
    public Delivery getByOrder(String orderId) {
        LambdaQueryWrapper<Delivery> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Delivery::getOrderId, orderId);
        Delivery delivery = getOne(wrapper);
        return delivery;
    }

}
