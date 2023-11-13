package net.stepbooks.domain.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.service.OrderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT_QUEUE;


@Slf4j
@Component
@RequiredArgsConstructor
public class DelayQueueMessageConsumer {

    public static final int FIXED_DELAY_TIME = 1000;
    private final DelayQueueMessageProducer delayQueueMessageProducer;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;


    @Async("CustomAsyncExecutor")
    @Scheduled(fixedDelay = FIXED_DELAY_TIME)
    public void cancelOrderAsPaymentTimeout() {
//        if (log.isDebugEnabled()) {
//            log.debug("Start to do cancelOrderAsPaymentTimeout...");
//        }
        String recordId = delayQueueMessageProducer.getDelayQueue(ORDER_PAYMENT_TIMEOUT_QUEUE);
        if (recordId != null) {
            log.info("get content to do cancelOrderAsPaymentTimeout task [{}]...", recordId);
            physicalOrderServiceImpl.autoCancelWhenPaymentTimeout(recordId);
            virtualOrderServiceImpl.autoCancelWhenPaymentTimeout(recordId);
        }
    }

}
