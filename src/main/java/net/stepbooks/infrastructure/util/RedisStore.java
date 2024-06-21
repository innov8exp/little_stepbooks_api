package net.stepbooks.infrastructure.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用于管理全部存放在Redis中的内存变量
 */

@Component
@RequiredArgsConstructor
public class RedisStore {

    private static final int TWO_MINUTES_SECONDS = 120;

    private final RedissonClient redissonClient;

    public void set(String key, Object value) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    public void setWithExpiration(String key, Object value, long ttl, TimeUnit timeUnit) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value, ttl, timeUnit);
    }

    public void setWithTwoMinutesExpiration(String key, Object value) {
        setWithExpiration(key, value, TWO_MINUTES_SECONDS, TimeUnit.SECONDS);
    }

    public <T> T get(String key, Class<T> clazz) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    public boolean exists(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.isExists();
    }

    public void delete(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

}
