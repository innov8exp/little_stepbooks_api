package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.order.entity.OrderRefundRequest;
import net.stepbooks.domain.order.mapper.OrderRefundRequestMapper;
import net.stepbooks.domain.order.service.OrderRefundRequestService;
import org.springframework.stereotype.Service;

@Service
public class OrderRefundRequestServiceImpl extends ServiceImpl<OrderRefundRequestMapper, OrderRefundRequest>
        implements OrderRefundRequestService {
}
