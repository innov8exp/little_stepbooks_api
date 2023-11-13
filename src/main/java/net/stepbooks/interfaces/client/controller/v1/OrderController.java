package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.interfaces.client.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class OrderController {

    private final ContextManager contextManager;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;
    private final OrderOpsService orderOpsService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        User user = contextManager.currentUser();
        CreateOrderDto orderDto = BaseAssembler.convert(placeOrderDto, CreateOrderDto.class);
        orderDto.setUserId(user.getId());
        Product product = productService.getProductBySkuCode(orderDto.getSkuCode());
        if (ProductNature.PHYSICAL.equals(product.getProductNature())) {
            physicalOrderServiceImpl.createOrder(orderDto);
            // 虚拟产品
        } else if (ProductNature.VIRTUAL.equals(product.getProductNature())) {
            virtualOrderServiceImpl.createOrder(orderDto);
        } else {
            throw new BusinessException(ErrorCode.PRODUCT_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<IPage<OrderInfoDto>> getUserOrderHistory(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize) {
        String userId = contextManager.currentUser().getId();
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> ordersByCriteria = orderOpsService.findOrdersByUser(page, userId);
        return ResponseEntity.ok(ordersByCriteria);
    }

    @GetMapping("/{code}/unpaid-remaining-time")
    public ResponseEntity<Long> getOrderUnpaidRemainingTime(@PathVariable String code) {
        long remainingTime = orderOpsService.getUnpaidRemainingTime(code);
        return ResponseEntity.ok(remainingTime);
    }

    @PutMapping("/{code}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.cancelOrder(order.getId());
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.cancelOrder(order.getId());
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{code}/mock/payment-callback")
    public ResponseEntity<?> mockPayOrder(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.paymentCallback(order);
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.paymentCallback(order);
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }

        return ResponseEntity.ok().build();
    }
}
