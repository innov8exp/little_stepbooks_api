package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.order.entity.OrderBook;
import net.stepbooks.domain.order.mapper.OrderBookMapper;
import net.stepbooks.domain.order.service.OrderBookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderBookServiceImpl extends ServiceImpl<OrderBookMapper, OrderBook> implements OrderBookService {

    private final OrderBookMapper orderBookMapper;

    @Override
    public List<Book> findUserOrderBooks(String userId) {
        return orderBookMapper.findUserOrderBooks(userId);
    }
}
