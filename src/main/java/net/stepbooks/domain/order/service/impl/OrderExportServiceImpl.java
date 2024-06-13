package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.service.OrderExportService;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.OrderExportDto;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderExportServiceImpl implements OrderExportService {

    private final OrderOpsService orderOpsService;

    @Override
    public List<OrderExportDto> export(String orderCode, String username, String state, LocalDate startDate, LocalDate endDate) {

        Page<OrderInfoDto> page = Page.of(1, AppConstants.MAX_PAGE_SIZE);
        IPage<OrderInfoDto> orders = orderOpsService.findOrdersByCriteria(page, orderCode, username, state, startDate, endDate);

        List<OrderInfoDto> records = orders.getRecords();

        List<OrderExportDto> data = new ArrayList<>();

        for (OrderInfoDto orderInfoDto : records) {
            OrderExportDto orderExportDto = BaseAssembler.convert(orderInfoDto, OrderExportDto.class);
            data.add(orderExportDto);
        }

        return data;
    }

}
