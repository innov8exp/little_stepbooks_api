package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.enums.DeliveryMethod;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderInventoryLog;
import net.stepbooks.domain.order.entity.OrderProduct;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderInventoryLogService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.util.OrderUtil;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT_BUFFER;
import static net.stepbooks.infrastructure.AppConstants.PHYSICAL_ORDER_CODE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhysicalOrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final StateMachine<OrderState, OrderEvent, Order> physicalOrderStateMachine;

    private final RedisDistributedLocker redisDistributedLocker;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final OrderProductService orderProductService;
    private final OrderInventoryLogService orderInventoryLogService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderDto orderDto) {
//        entity.setOrderNo(IdWorker.getIdStr());
        Product product = productService.getProductBySkuCode(orderDto.getSkuCode());
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        // 有库存商品，先锁库存，再下单
        String productId = product.getId();
        boolean res = redisDistributedLocker.tryLock(productId);
        if (!res) {
            log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁失败", productId);
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED, "Server is busy, please try again later");
        }
        log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁成功", productId);
        try {
            // 锁库存
            Inventory inventory = inventoryService.decreaseInventory(productId, orderDto.getQuantity());
            // 创建订单
            String orderCode = OrderUtil.generateOrderNo(PHYSICAL_ORDER_CODE_PREFIX);
            Order order = OrderUtil.buildOrder(orderDto, product, orderCode);
            log.info("OrderNo:" + order.getOrderCode());
            orderMapper.insert(order);
            // 创建订单商品
            OrderProduct orderProduct = buildOrderProduct(orderDto, order, productId);
            orderProductService.save(orderProduct);
            updateOrderState(order.getId(), OrderEvent.PLACE_SUCCESS);
            // 创建物流信息
            Delivery delivery = buildDelivery(order, orderDto);
            deliveryService.save(delivery);
            // 记录库存变更日志
            OrderInventoryLog orderInventoryLog = buildOrderInventoryLog(orderDto, order, productId, inventory.getId());
            orderInventoryLogService.save(orderInventoryLog);
        } catch (OptimisticLockingFailureException e) {
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED);
        } finally {
            redisDistributedLocker.unlock(productId);
            log.info("线程 PRODUCT_STOCK_LOCK_{} 释放锁成功", productId);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderState(String id, OrderEvent orderEvent) {
        String machineId = physicalOrderStateMachine.getMachineId();
        log.debug("订单状态机：{}", machineId);
        Order order = orderMapper.selectById(id);
        OrderState state = physicalOrderStateMachine.fireEvent(order.getState(), orderEvent, order);
        order.setState(state);
        orderMapper.updateById(order);
        return order;
    }

    @Override
    public void cancelTimeoutOrders() {
        orderMapper.selectList(Wrappers.<Order>lambdaQuery().eq(Order::getState, OrderState.PLACED))
                .forEach(order -> {
                    if (order.getCreatedAt()
                            .plusSeconds(order.getPaymentTimeoutDuration())
                            .plusSeconds(ORDER_PAYMENT_TIMEOUT_BUFFER)
                            .isBefore(LocalDateTime.now())) {
                        log.info("Find already payment timeout and uncancelled order [{}], start to cancel it...", order.getId());
                        updateOrderState(order.getId(), OrderEvent.PAYMENT_TIMEOUT);
                    }
                });
    }

    @Override
    public void autoCancelWhenPaymentTimeout(String recordId) {
        updateOrderState(recordId, OrderEvent.PAYMENT_TIMEOUT);
    }

    @Override
    public void closeOrder(String id) {
        updateOrderState(id, OrderEvent.ADMIN_MANUAL_CLOSE);
    }

    @Override
    public void cancelOrder(String id) {
        updateOrderState(id, OrderEvent.USER_MANUAL_CANCEL);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void paymentCallback(Order order) {
        Order updatedOrder = updateOrderState(order.getId(), OrderEvent.PAYMENT_SUCCESS);
        updatedOrder.setPaymentStatus(PaymentStatus.PAID);
        orderMapper.updateById(updatedOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shipOrder(String id, DeliveryInfoDto deliveryInfoDto) {
        updateOrderState(id, OrderEvent.SHIP_SUCCESS);
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery().eq(Delivery::getOrderId, id));
        delivery.setShipperUserId(deliveryInfoDto.getShipperUserId());
        delivery.setDeliveryCompany(deliveryInfoDto.getDeliveryCompany());
        delivery.setDeliveryCode(deliveryInfoDto.getDeliveryCode());
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERING);
        deliveryService.updateById(delivery);
    }

    @Override
    public void signOrder(String id) {
        updateOrderState(id, OrderEvent.SIGN_SUCCESS);
    }

    @Override
    public void refundRequest(String id) {
        updateOrderState(id, OrderEvent.REFUND_REQUEST);
    }

    @Override
    public void refundApprove(String id) {
        updateOrderState(id, OrderEvent.REFUND_APPROVE);
    }


    private OrderInventoryLog buildOrderInventoryLog(CreateOrderDto orderDto, Order order,
                                                     String productId, String inventoryId) {
        return OrderInventoryLog.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .inventoryId(inventoryId)
                .productId(productId)
                .skuCode(orderDto.getSkuCode())
                .quantity(orderDto.getQuantity())
                .build();
    }

    private OrderProduct buildOrderProduct(CreateOrderDto orderDto, Order order, String productId) {
        return OrderProduct.builder()
                .orderId(order.getId())
                .productId(productId)
                .quantity(orderDto.getQuantity())
                .build();
    }

    private Delivery buildDelivery(Order order, CreateOrderDto orderDto) {
        return Delivery.builder()
                .deliveryMethod(DeliveryMethod.EXPRESS)
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .userId(orderDto.getUserId())
                .deliveryStatus(DeliveryStatus.WAITING)
                .recipientName(orderDto.getRecipientName())
                .recipientPhone(orderDto.getRecipientPhone())
                .recipientAddress(orderDto.getRecipientAddress())
                .build();
    }
}
