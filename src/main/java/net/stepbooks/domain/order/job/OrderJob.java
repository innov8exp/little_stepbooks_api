package net.stepbooks.domain.order.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.infrastructure.config.AppConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderJob implements Job {

    //private final OrderService physicalOrderServiceImpl;
    //private final OrderService virtualOrderServiceImpl;
    private final OrderService mixedOrderServiceImpl;

    private final RefundRequestService refundRequestService;

    private final AppConfig appConfig;

    @Override
    public void execute(JobExecutionContext context) {
        log.debug("Order Job is running...");
        //physicalOrderServiceImpl.cancelTimeoutOrders();
        //virtualOrderServiceImpl.cancelTimeoutOrders();
        try {
            mixedOrderServiceImpl.cancelTimeoutOrders();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        try {
            if (!appConfig.isMock()) {
                refundRequestService.refundApprovedOrders();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
