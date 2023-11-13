package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.DeliveryCompany;
import net.stepbooks.domain.order.service.OrderEventLogService;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.DeliveryCompanyDto;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/v1/orders")
@SecurityRequirement(name = "Admin Authentication")
@RequiredArgsConstructor
public class MOrderController {

    private final OrderOpsService orderOpsService;
    private final ProductService productService;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;
    private final OrderEventLogService orderEventLogService;
    private final OrderProductService orderProductService;
    private final ContextManager contextManager;

    @GetMapping("/search")
    public ResponseEntity<IPage<OrderInfoDto>> getAllOrders(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            @RequestParam(required = false) String orderNo,
                                                            @RequestParam(required = false) String username) {
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> orders = orderOpsService.findOrdersByCriteria(page, orderNo, username);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        Order order = orderOpsService.findOrderById(id);
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
        Order order = orderOpsService.findOrderById(id);
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.closeOrder(id);
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.closeOrder(id);
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/shipment")
    public ResponseEntity<?> shipOrder(@PathVariable String id, @RequestBody DeliveryInfoDto deliveryInfoDto) {
        AdminUser adminUser = contextManager.currentAdminUser();
        deliveryInfoDto.setShipperUserId(adminUser.getId());
        physicalOrderServiceImpl.shipOrder(id, deliveryInfoDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/sign")
    public ResponseEntity<?> signOrder(@PathVariable String id) {
        physicalOrderServiceImpl.signOrder(id);
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
