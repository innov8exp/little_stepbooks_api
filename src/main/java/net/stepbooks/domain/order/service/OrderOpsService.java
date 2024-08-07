package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderOpsService {

    IPage<OrderInfoDto> findOrdersByCriteria(StoreType storeType, Page<OrderInfoDto> page, String orderNo,
                                             String username, String state, LocalDateTime startDate, LocalDateTime endDate);

    IPage<OrderInfoDto> findOrdersByUser(StoreType storeType, Page<OrderInfoDto> page, String userId,
                                         OrderState state, String keyword);

    Order findOrderById(String id);

    Order findOrderByCode(String code);

    OrderInfoDto findOrderByCodeAndUser(String code, String userId);

    long getUnpaidRemainingTime(String code);

    boolean checkCourseInUserOrder(String userId, String courseId);

    boolean checkBookInUserOrder(String userId, String bookId);

    List<Book> getUserBooks(String userId);

    List<Course> getUserCourses(String userId);
}
