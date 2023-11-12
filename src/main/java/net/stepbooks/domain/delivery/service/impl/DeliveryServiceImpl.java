package net.stepbooks.domain.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.mapper.DeliveryMapper;
import net.stepbooks.domain.delivery.service.DeliveryService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
}
