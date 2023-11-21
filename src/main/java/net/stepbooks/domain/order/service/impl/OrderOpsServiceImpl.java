package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.model.BaseDto;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT;

@Service
@RequiredArgsConstructor
public class OrderOpsServiceImpl implements OrderOpsService {

    private final OrderMapper orderMapper;
    private final OrderProductService orderProductService;
    private final DeliveryService deliveryService;

    @Override
    public IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderCode, String username) {
        return orderMapper.findByCriteria(page, orderCode, username);
    }

    @Override
    public IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId, OrderState state) {
        // Step 1: Retrieve orders based on user and state
        IPage<OrderInfoDto> orderInfoDto = orderMapper.findPageByUser(page, userId, state);

        // Step 2: Extract order IDs from the retrieved orders
        List<String> orderIds = orderInfoDto.getRecords().stream()
                .map(BaseDto::getId)
                .toList();

        // Step 3: Retrieve order products for the extracted order IDs
        List<OrderProductDto> productDtos = orderProductService.findByOrderIds(orderIds);

        // Step 4: Enhance orderInfoDto by adding products to each order
        List<OrderInfoDto> enhancedOrderInfoDtos = orderInfoDto.getRecords().stream()
                .peek(order -> {
                    productDtos.stream()
                            .filter(product -> product.getOrderId().equals(order.getId())).findFirst()
                            .ifPresent(order::setProduct);
                })
                .toList();

        // Step 5: Update orderInfoDto with the enhanced order information
        orderInfoDto.setRecords(enhancedOrderInfoDtos);

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
    public OrderInfoDto findOrderByCodeAndUser(String code, String userId) {
        // Step 1: Retrieve the order based on order code and user ID
        Order order = orderMapper.selectOne(Wrappers.<Order>lambdaQuery()
                .eq(Order::getOrderCode, code)
                .eq(Order::getUserId, userId));

        // Step 2: Check if the order exists, throw an exception if not found
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        // Step 3: Retrieve order products for the order
        OrderProductDto product = orderProductService.findByOrderId(order.getId());

        // Step 4: Convert Order entity to OrderInfoDto
        OrderInfoDto orderInfoDto = BaseAssembler.convert(order, OrderInfoDto.class);
        orderInfoDto.setProduct(product);

        // Step 5: Retrieve the delivery information for the order
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery()
                .eq(Delivery::getOrderId, order.getId()));
        orderInfoDto.setDelivery(delivery);

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

}
