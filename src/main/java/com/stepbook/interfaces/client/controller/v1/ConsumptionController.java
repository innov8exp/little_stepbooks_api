package com.stepbook.interfaces.client.controller.v1;

import com.stepbook.interfaces.admin.dto.ConsumptionInfoDto;
import com.stepbook.domain.order.service.ConsumptionService;
import com.stepbook.infrastructure.util.ContextManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/consumptions")
@RequiredArgsConstructor
public class ConsumptionController {

    private final ConsumptionService consumptionService;
    private final ContextManager contextManager;

    @GetMapping("/user")
    public ResponseEntity<IPage<ConsumptionInfoDto>> getUserConsumeHistory(@RequestParam int currentPage,
                                                                           @RequestParam int pageSize) {
        String userId = contextManager.currentUser().getId();
        Page<ConsumptionInfoDto> page = Page.of(currentPage, pageSize);
        IPage<ConsumptionInfoDto> consumptionInfoDtoIPage = consumptionService.findByUser(page, userId);
        return ResponseEntity.ok(consumptionInfoDtoIPage);
    }
}
