package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderEntity;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.application.dto.admin.OrderInfoDto;
import net.stepbooks.application.dto.client.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class OrderController {

    private final ContextManager contextManager;
    private final OrderService orderService;

    @GetMapping("/user")
    public ResponseEntity<IPage<OrderInfoDto>> getUserOrderHistory(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize) {
        String userId = contextManager.currentUser().getId();
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> ordersByCriteria = orderService.findOrdersByUser(page, userId);
        return ResponseEntity.ok(ordersByCriteria);
    }

    @PostMapping("/chapter")
    public ResponseEntity<?> orderChapter(@RequestBody OrderDto orderDto) {
        User user = contextManager.currentUser();
        OrderEntity orderEntity = BaseAssembler.convert(orderDto, OrderEntity.class);
        orderEntity.setUserId(user.getId());
        orderService.createOrder(orderEntity);
        return ResponseEntity.ok().build();
    }
}
