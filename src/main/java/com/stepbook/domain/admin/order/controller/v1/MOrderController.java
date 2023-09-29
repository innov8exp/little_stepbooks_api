package com.stepbook.domain.admin.order.controller.v1;

import com.stepbook.domain.admin.order.dto.OrderInfoDto;
import com.stepbook.domain.order.dto.OrderDto;
import com.stepbook.domain.order.entity.OrderEntity;
import com.stepbook.domain.order.service.OrderService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/orders")
public class MOrderController {

    private final OrderService orderService;

    public MOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        OrderEntity entity = BaseAssembler.convert(orderDto, OrderEntity.class);
        orderService.createOrder(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody OrderDto orderDto) {
        OrderEntity entity = BaseAssembler.convert(orderDto, OrderEntity.class);
        orderService.updateOrder(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<OrderInfoDto>> getAllOrders(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            @RequestParam(required = false) String orderNo,
                                                            @RequestParam(required = false) String username) {
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> orders = orderService.findOrdersByCriteria(page, orderNo, username);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getAllOrder(@PathVariable String id) {
        OrderEntity orderEntity = orderService.findOrder(id);
        return ResponseEntity.ok(BaseAssembler.convert(orderEntity, OrderDto.class));
    }

}
