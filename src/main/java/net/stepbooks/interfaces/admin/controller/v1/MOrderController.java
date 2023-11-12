package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.DeliveryCompany;
import net.stepbooks.domain.order.service.OrderEventLogService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.DeliveryCompanyDto;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;
import net.stepbooks.interfaces.client.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/v1/orders")
@SecurityRequirement(name = "Admin Authentication")
@RequiredArgsConstructor
public class MOrderController {

    private final OrderService orderService;
    private final OrderEventLogService orderEventLogService;
    private final OrderProductService orderProductService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody OrderDto orderDto) {
        Order entity = BaseAssembler.convert(orderDto, Order.class);
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
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        Order order = orderService.getById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/ship-companies")
    public ResponseEntity<List<DeliveryCompanyDto>> getShipCompanies() {
        ArrayList<DeliveryCompanyDto> deliveryCompanies = new ArrayList<>();
        for (DeliveryCompany deliveryCompany : DeliveryCompany.values()) {
            DeliveryCompanyDto companyDto = DeliveryCompanyDto.builder().code(deliveryCompany.getKey())
                    .name(deliveryCompany.getValue()).build();
            deliveryCompanies.add(companyDto);
        }
        return ResponseEntity.ok(deliveryCompanies);
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<?> closeOrder(@PathVariable String id) {
        orderService.closeOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/shipment")
    public ResponseEntity<?> shipOrder(@PathVariable String id, @RequestBody DeliveryInfoDto deliveryInfoDto) {
        orderService.shipOrder(id, deliveryInfoDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/sign")
    public ResponseEntity<?> signOrder(@PathVariable String id) {
        orderService.signOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/event-logs")
    public ResponseEntity<List<OrderEventLog>> getOrderEventLog(@PathVariable String id) {
        List<OrderEventLog> orderEventLogs = orderEventLogService.findByOrderId(id);
        return ResponseEntity.ok(orderEventLogs);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<OrderProductDto>> getOrderProducts(@PathVariable String id) {
        List<OrderProductDto> orderProducts = orderProductService.findByOrderId(id);
        return ResponseEntity.ok(orderProducts);
    }
}
