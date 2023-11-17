package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.order.enums.RequestStatus;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.RefundRequestCreateDto;
import net.stepbooks.interfaces.client.dto.ReturnDeliveryInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/refund-requests")
public class RefundRequestController {

    private final RefundRequestService refundRequestService;
    private final ContextManager contextManager;
    private final OrderOpsService orderOpsService;

    @PostMapping
    @Operation(summary = "创建退款申请")
    public ResponseEntity<?> createRequest(@RequestBody RefundRequestCreateDto refundRequestCreateDto) {
        // 检查是否有未完成的退款申请或已经退款成功的订单
        String orderCode = refundRequestCreateDto.getOrderCode();
        Order order = orderOpsService.findOrderByCode(orderCode);
        if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            throw new BusinessException(ErrorCode.VIRTUAL_ORDER_NOT_SUPPORT_REFUND);
        }
        boolean existsRefundRequest = refundRequestService.existsRefundRequest(orderCode);
        if (existsRefundRequest) {
            throw new BusinessException(ErrorCode.REFUND_REQUEST_EXISTS);
        }
        User currentUser = contextManager.currentUser();
        String userId = currentUser.getId();
        refundRequestService.createRefundRequest(refundRequestCreateDto, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "取消退款申请")
    public ResponseEntity<?> cancelRequest(@PathVariable String id) {
        RefundRequest refundRequest = refundRequestService.getById(id);
        refundRequest.setRequestStatus(RequestStatus.CANCELLED);
        refundRequestService.updateById(refundRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询退款申请详情")
    public ResponseEntity<RefundRequest> getRequest(@PathVariable String id) {
        RefundRequest refundRequest = refundRequestService.getById(id);
        return ResponseEntity.ok(refundRequest);
    }

    @PostMapping("/{id}/delivery")
    @Operation(summary = "填写退货物流信息")
    public ResponseEntity<?> fillDeliveryInfo(@PathVariable String id, @RequestBody ReturnDeliveryInfoDto deliveryInfo) {
        refundRequestService.fillDeliveryInfo(id, deliveryInfo);
        return ResponseEntity.ok().build();
    }

}
