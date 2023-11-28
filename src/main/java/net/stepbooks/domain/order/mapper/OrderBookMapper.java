package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.order.entity.OrderBook;

import java.util.List;

public interface OrderBookMapper extends BaseMapper<OrderBook> {

    List<Book> findUserOrderBooks(String userId);
}
