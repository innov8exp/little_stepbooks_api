package net.stepbooks.domain.order.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.infrastructure.config.AppConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderJob implements Job {

    //private final OrderService physicalOrderServiceImpl;
    //private final OrderService virtualOrderServiceImpl;
    private final OrderService mixedOrderServiceImpl;

    private final RefundRequestService refundRequestService;

    private final AppConfig appConfig;

    private final RedissonClient redissonClient;

    private static final int THREE = 3;

    private static final int SLEEP_TIME = 10000;

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

        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void execute(JobExecutionContext context) {

        RLock lock = redissonClient.getLock("OrderJob");
        try {
            // 尝试获取锁,超时时间为3秒
            if (lock.tryLock(THREE, TimeUnit.SECONDS)) {
                // 执行任务的逻辑
                log.debug("Order Job start ...");
                executeImpl();
            } else {
                // 无法获取锁,说明任务正在被其他节点执行
                log.info("Order Job already in progress");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 无论如何都要释放锁
            lock.unlock();
        }
    }
}
