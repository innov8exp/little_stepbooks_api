package net.stepbooks.domain.order.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.infrastructure.config.AppConfig;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.infrastructure.util.RedisStore;
import net.stepbooks.interfaces.KeyConstants;
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

    private final RedisDistributedLocker redisDistributedLocker;

    private final RedisStore redisStore;

    private void executeImpl() {

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

    @Override
    public void execute(JobExecutionContext context) {

        boolean res = redisDistributedLocker.tryLock("OrderJob");
        if (!res) {
            log.info("OrderJob already in progress");
            return;
        }
        try {
            log.debug("OrderJob start ...");
            if (!redisStore.exists(KeyConstants.FLAG_ORDER_JOB)) {
                redisStore.setWithTwoMinutesExpiration(KeyConstants.FLAG_ORDER_JOB, true);
                executeImpl();
            } else {
                log.info("OrderJob execute too frequently");
            }
        } finally {
            redisDistributedLocker.unlock("OrderJob");
        }
    }
}
