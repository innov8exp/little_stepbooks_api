package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;

import java.util.List;

public interface OrderOpsService {

    IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId, OrderState state);

    Order findOrderById(String id);

    Order findOrderByCode(String code);

    OrderInfoDto findOrderByCodeAndUser(String code, String userId);

    long getUnpaidRemainingTime(String code);

    boolean checkCourseInUserOrder(String userId, String courseId);

    boolean checkBookInUserOrder(String userId, String bookId);

    List<Book> getUserBooks(String userId);

    List<Course> getUserCourses(String userId);
}
