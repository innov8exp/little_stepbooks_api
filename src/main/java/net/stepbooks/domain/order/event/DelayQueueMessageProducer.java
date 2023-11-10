package net.stepbooks.domain.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.exception.ServiceException;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelayQueueMessageProducer {

    private final RedissonClient redissonClient;

    public void addDelayQueue(String queueCode, String recordId, long delay, TimeUnit timeUnit) {
        try {
            if (delay <= 0) {
                delay = 1;
            }
            RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(queueCode);
            RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            delayedQueue.offer(recordId, delay, timeUnit);
            log.info("(Added to delay queue successfully) key：{}，value：{}，delay：{}",
                    queueCode, recordId, timeUnit.toSeconds(delay) + "秒");
        } catch (Exception e) {
            log.error("Failed to add to delay queue.", e);
            throw new ServiceException(ErrorCode.SCHEDULE_TASK_ERROR);
        }
    }

    public String getDelayQueue(String queueCode) {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(queueCode);
        try {
            if (blockingDeque.isEmpty()) {
//                if (log.isDebugEnabled()) {
//                    log.debug("there is no data in {}", queueCode);
//                }
                return null;
            } else {
                return blockingDeque.take();
            }
        } catch (InterruptedException e) {
            throw new ServiceException(ErrorCode.SCHEDULE_TASK_ERROR);
        }
    }

}
