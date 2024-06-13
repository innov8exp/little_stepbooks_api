package net.stepbooks.domain.order.service;

import net.stepbooks.interfaces.admin.dto.OrderExportDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderExportService {
    List<OrderExportDto> export(String orderCode, String username, String state, LocalDate startDate, LocalDate endDate);
}
