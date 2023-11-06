package net.stepbooks.infrastructure.util;

import lombok.experimental.UtilityClass;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.exception.ServiceException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@UtilityClass
public class RedisLockUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RedisLockUtils.class);

    private static final long WAIT_TIME = 15;
    private static final long LEASE_TIME = 10;

    public static <T> T operateWithLock(final String code, Supplier<T> supplier) {
        RedissonClient redissonClient = ApplicationContextUtils.getBean(RedissonClient.class);
        if (redissonClient == null) {
            throw new ServiceException(ErrorCode.REDIS_CONNECT_ERROR);
        }
        RLock lock = redissonClient.getLock(code);
        try {
            LOG.info("operateWithLock tryLock with lock code {}.", code);
            boolean result = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!result) {
                LOG.error("operateWithLock tryLock with lock code {} failed.", code);
                throw new ServiceException(ErrorCode.REDIS_CONNECT_ERROR);
            }
            return supplier.get();
        } catch (ServiceException e) {
            LOG.error("operateWithLock tryLock exception with lock code {}, exception: ", code, e);
            throw e;
        } catch (Exception e) {
            LOG.error("operateWithLock tryLock exception with lock code {}, exception: ", code, e);
            throw new ServiceException(ErrorCode.REDIS_CONNECT_ERROR);
        } finally {
            LOG.info("operateWithLock done with lock code {}.", code);
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
