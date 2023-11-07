package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.application.dto.admin.OrderInfoDto;
import net.stepbooks.application.dto.client.PlaceOrderDto;
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
        orderService.createOrder(order);
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

    @GetMapping("/{id}/unpaid-remaining-time")
    public ResponseEntity<Long> getOrderUnpaidRemainingTime(@PathVariable String id) {
        long remainingTime = orderService.getUnpaidRemainingTime(id);
        return ResponseEntity.ok(remainingTime);
    }
}
