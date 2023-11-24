package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.order.entity.OrderBook;
import net.stepbooks.domain.order.mapper.OrderBookMapper;
import net.stepbooks.domain.order.service.OrderBookService;
import org.springframework.stereotype.Service;

@Service
public class OrderBookServiceImpl extends ServiceImpl<OrderBookMapper, OrderBook> implements OrderBookService {
}
