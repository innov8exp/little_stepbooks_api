package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_ORDER_EVENT_LOG")
public class OrderEventLog extends BaseEntity {

    private String orderId;
    private String orderCode;
    private OrderState fromState;
    private OrderState toState;
    private OrderEvent eventType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;
}
