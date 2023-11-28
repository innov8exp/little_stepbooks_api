package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.order.entity.OrderBook;

import java.util.List;

public interface OrderBookService extends IService<OrderBook> {

    List<Book> findUserOrderBooks(String userId);
}
