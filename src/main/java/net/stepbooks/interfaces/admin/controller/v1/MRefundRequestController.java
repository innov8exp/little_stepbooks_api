package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.interfaces.admin.dto.RefundAmountDto;
import net.stepbooks.interfaces.admin.dto.RejectReasonDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
@RequestMapping("/admin/v1/refund-requests")
public class MRefundRequestController {

    private final RefundRequestService refundRequestService;

    @GetMapping("/search")
    public ResponseEntity<IPage<RefundRequest>> searchRefundRequests(@RequestParam int currentPage,
                                                                     @RequestParam int pageSize,
                                                                     @RequestParam(required = false) String orderCode) {
        Page<RefundRequest> page = Page.of(currentPage, pageSize);
        IPage<RefundRequest> refundRequests = refundRequestService.getPagedRefundRequests(page, orderCode);
        return ResponseEntity.ok(refundRequests);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRefundRequest(@PathVariable String id, @RequestBody RefundAmountDto refundAmountDto) {
        refundRequestService.approveRefundRequest(id, refundAmountDto.getRefundAmount());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRefundRequest(@PathVariable String id, @RequestBody RejectReasonDto rejectReasonDto) {
        refundRequestService.rejectRefundRequest(id, rejectReasonDto.getRejectReason());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/return-delivery-sign")
    public ResponseEntity<?> signRefundDelivery(@PathVariable String id) {
        refundRequestService.signRefundDelivery(id);
        return ResponseEntity.ok().build();
    }
}
