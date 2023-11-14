package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderInventoryLog;
import net.stepbooks.domain.order.service.OrderInventoryLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/order-inventory-logs")
public class MOrderInventoryLogController {

    private final OrderInventoryLogService orderInventoryLogService;

    @GetMapping
    public ResponseEntity<IPage<OrderInventoryLog>> getPagedOrderInventoryLogs(@RequestParam int currentPage,
                                                                               @RequestParam int pageSize,
                                                                               @RequestParam(required = false) String skuCode,
                                                                               @RequestParam(required = false) String orderCode) {
        Page<OrderInventoryLog> page = Page.of(currentPage, pageSize);
        IPage<OrderInventoryLog> inventories = orderInventoryLogService.findInPagingByCriteria(page, skuCode, orderCode);
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderInventoryLog> getOrderInventoryLogById(@PathVariable String id) {
        OrderInventoryLog orderInventoryLog = orderInventoryLogService.getById(id);
        return ResponseEntity.ok(orderInventoryLog);
    }
}
