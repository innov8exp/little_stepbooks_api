package net.stepbooks.domain.order.service;

import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.interfaces.admin.dto.OrderExportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderExportService {
    List<OrderExportDto> export(StoreType storeType, String orderCode, String username, String state,
                                LocalDateTime startDateTime, LocalDateTime endDateTime);

    void dailyExport();
}
