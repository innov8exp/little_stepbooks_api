package co.botechservices.novlnovl.domain.order.controller.v1;

import co.botechservices.novlnovl.domain.admin.order.dto.OrderInfoDto;
import co.botechservices.novlnovl.domain.order.dto.OrderDto;
import co.botechservices.novlnovl.domain.order.entity.OrderEntity;
import co.botechservices.novlnovl.domain.order.service.OrderService;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.manager.ContextManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
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
        UserEntity userEntity = contextManager.currentUser();
        OrderEntity orderEntity = BaseAssembler.convert(orderDto, OrderEntity.class);
        orderEntity.setUserId(userEntity.getId());
        orderService.createOrder(orderEntity);
        return ResponseEntity.ok().build();
    }
}
