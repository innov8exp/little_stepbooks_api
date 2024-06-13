package net.stepbooks.interfaces.admin.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import net.stepbooks.domain.order.enums.OrderState;

import java.time.LocalDateTime;

@Data
public class OrderExportDto {

    @CsvBindByName(column = "订单号")
    private String orderCode;

    @CsvBindByName(column = "订单状态")
    private OrderState state;

    @CsvBindByName(column = "订单创建时间")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
