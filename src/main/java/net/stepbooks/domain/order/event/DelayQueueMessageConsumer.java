package net.stepbooks.domain.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.service.OrderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static net.stepbooks.infrastructure.AppConstants.ORDER_UNPAID_TIMEOUT_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class DelayQueueMessageConsumer {

    public static final int FIXED_DELAY_TIME = 1000;
    private final OrderService orderService;
    private final DelayQueueMessageProducer delayQueueMessageProducer;

    @Async("consumerTaskExecutor")
    @Scheduled(fixedDelay = FIXED_DELAY_TIME)
    public void cancelOrderAsDepositPaymentTimeout() {
        if (log.isDebugEnabled()) {
            log.debug("Start to do cancelOrderAsDepositPaymentTimeout...");
        }
        String recordId = delayQueueMessageProducer.getDelayQueue(ORDER_UNPAID_TIMEOUT_QUEUE);
        if (recordId != null) {
            log.info("get content to do cancelOrderAsDepositPaymentTimeout task [{}]...", recordId);
            orderService.cancelOrder(recordId);
        }
    }

}
