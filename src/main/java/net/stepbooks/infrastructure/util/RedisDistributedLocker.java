package net.stepbooks.infrastructure.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisDistributedLocker {

    private final RedissonClient redissonClient;

    public RLock lock(String lockKey) {
        RLock rlock = redissonClient.getLock(lockKey);
        rlock.lock();
        return rlock;
    }

    public RLock lock(String lockKey, long leaseTime) {
        RLock rlock = redissonClient.getLock(lockKey);
        rlock.lock(leaseTime, TimeUnit.SECONDS);
        return rlock;
    }

    public RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock rlock = redissonClient.getLock(lockKey);
        rlock.lock(timeout, unit);
        return rlock;
    }

    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock rlock = redissonClient.getLock(lockKey);
        try {
            return rlock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public boolean tryLock(String lockKey) {
        RLock rlock = redissonClient.getLock(lockKey);
        return rlock.tryLock();
    }

    public void unlock(String lockKey) {
        RLock rlock = redissonClient.getLock(lockKey);
        if (rlock.isHeldByCurrentThread()) {
            rlock.unlock();
        }
    }

    public void unlock(RLock rLock) {
        rLock.unlock();
    }
}
