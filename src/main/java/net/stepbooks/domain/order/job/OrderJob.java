package net.stepbooks.domain.order.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.service.OrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderJob implements Job {

    private final OrderService orderService;

    @Override
    public void execute(JobExecutionContext context) {
        log.debug("Order Job is running...");
        orderService.cancelTimeoutOrders();
    }
}
