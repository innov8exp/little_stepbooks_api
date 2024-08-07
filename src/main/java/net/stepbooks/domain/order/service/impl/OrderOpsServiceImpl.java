package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderBook;
import net.stepbooks.domain.order.entity.OrderCourse;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.*;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT;

@Service
@RequiredArgsConstructor
public class OrderOpsServiceImpl implements OrderOpsService {

    private final OrderMapper orderMapper;
    private final OrderSkuService orderSkuService;
    private final DeliveryService deliveryService;
    private final OrderBookService orderBookService;
    private final OrderCourseService orderCourseService;
    private final OrderEventLogService orderEventLogService;
    private final PaymentService paymentService;
    private final StateMachine<OrderState, OrderEvent, Order> physicalOrderStateMachine;
    private final StateMachine<OrderState, OrderEvent, Order> virtualOrderStateMachine;

    @Override
    public IPage<OrderInfoDto> findOrdersByCriteria(StoreType storeType, Page<OrderInfoDto> page, String orderCode,
                                                    String username, String state,
                                                    LocalDateTime startDate, LocalDateTime endDate) {
        return orderMapper.findByCriteria(storeType, page, orderCode, username, state, startDate, endDate);
    }

    @Override
    public IPage<OrderInfoDto> findOrdersByUser(StoreType storeType, Page<OrderInfoDto> page,
                                                String userId, OrderState state, String keyword) {
        IPage<OrderInfoDto> orderInfoDto = orderMapper.findPageByUser(storeType, page, userId, state, keyword);
        List<OrderInfoDto> enhancedOrders = orderInfoDto.getRecords().stream()
                .peek(orderInfo -> orderInfo.setSkus(orderSkuService.findOrderSkusByOrderId(orderInfo.getId()))).toList();
        orderInfoDto.setRecords(enhancedOrders);
        return orderInfoDto;
    }

    @Override
    public Order findOrderById(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    public Order findOrderByCode(String code) {
        Order order = orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderCode, code));
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderInfoDto findOrderByCodeAndUser(String code, String userId) {
        // Step 1: Retrieve the order based on order code and user ID
        Order order = orderMapper.selectOne(Wrappers.<Order>lambdaQuery()
                .eq(Order::getOrderCode, code)
                .eq(Order::getUserId, userId));

        // Step 2: Check if the order exists, throw an exception if not found
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        if (order.getState() == OrderState.PLACED) {
            Transaction transaction = paymentService.queryStatus(order.getOrderCode());
            if (transaction != null && transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
                if (order.getProductNature().equals(ProductNature.PHYSICAL)) {
                    physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_SUCCESS, order);
                } else if (order.getProductNature().equals(ProductNature.VIRTUAL)) {
                    virtualOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_SUCCESS, order);
                } else {
                    throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
                }
            }
        }
        // Step 3: Retrieve order products for the order
        // List<OrderProductDto> products = orderProductService.findByOrderId(order.getId());
        List<OrderSkuDto> skus = orderSkuService.findOrderSkusByOrderId(order.getId());

        // Step 4: Convert Order entity to OrderInfoDto
        OrderInfoDto orderInfoDto = BaseAssembler.convert(order, OrderInfoDto.class);
        // orderInfoDto.setProducts(products);
        orderInfoDto.setSkus(skus);

        // Step 5: Retrieve the delivery information for the order
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery()
                .eq(Delivery::getOrderId, order.getId()));
        orderInfoDto.setDelivery(delivery);

        // Step 6: Retrieve the event log information for the order
        List<OrderEventLog> eventLogs = orderEventLogService.findByOrderId(order.getId());
        orderInfoDto.setEventLogs(eventLogs);

        return orderInfoDto;
    }


    @Override
    public long getUnpaidRemainingTime(String code) {
        Order order = findOrderByCode(code);
        LocalDateTime createdAt = order.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        long seconds = duration.getSeconds();
        return seconds > ORDER_PAYMENT_TIMEOUT ? 0 : ORDER_PAYMENT_TIMEOUT - seconds;
    }

    @Override
    public boolean checkCourseInUserOrder(String userId, String courseId) {
        return orderCourseService.exists(Wrappers.<OrderCourse>lambdaQuery().eq(OrderCourse::getCourseId, courseId)
                .eq(OrderCourse::getUserId, userId));
    }

    @Override
    public boolean checkBookInUserOrder(String userId, String bookId) {
        return orderBookService.exists(Wrappers.<OrderBook>lambdaQuery().eq(OrderBook::getBookId, bookId)
                .eq(OrderBook::getUserId, userId));
    }

    @Override
    public List<Book> getUserBooks(String userId) {
        return orderBookService.findUserOrderBooks(userId);
    }

    @Override
    public List<Course> getUserCourses(String userId) {
        return orderCourseService.findUserOrderCourses(userId);
    }

}
