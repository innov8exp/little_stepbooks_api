package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.interfaces.client.dto.PlaceOrderDto;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class OrderController {

    private final ContextManager contextManager;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderDto orderDto) {
        User user = contextManager.currentUser();
        Order order = BaseAssembler.convert(orderDto, Order.class);
        order.setUserId(user.getId());
        CreateOrderDto createOrderDto = BaseAssembler.convert(orderDto, CreateOrderDto.class);
        createOrderDto.setUserId(user.getId());
        orderService.createOrder(createOrderDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<IPage<OrderInfoDto>> getUserOrderHistory(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize) {
        String userId = contextManager.currentUser().getId();
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> ordersByCriteria = orderService.findOrdersByUser(page, userId);
        return ResponseEntity.ok(ordersByCriteria);
    }

    @GetMapping("/{code}/unpaid-remaining-time")
    public ResponseEntity<Long> getOrderUnpaidRemainingTime(@PathVariable String code) {
        long remainingTime = orderService.getUnpaidRemainingTime(code);
        return ResponseEntity.ok(remainingTime);
    }

    @PutMapping("/{code}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderService.cancelOrder(order.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{code}/mock/payment-callback")
    public ResponseEntity<?> mockPayOrder(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderService.paymentCallback(order);
        return ResponseEntity.ok().build();
    }
}
