package net.stepbooks.infrastructure.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static net.stepbooks.infrastructure.AppConstants.*;

@UtilityClass
public class RandomNumberUtils {

    public static String getRandom(String currentDate, int length) {
        StringRedisTemplate stringRedisTemplate = ApplicationContextUtils.getBean(StringRedisTemplate.class);
        assert stringRedisTemplate != null;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Set<String> randomSet = new LinkedHashSet<>();
        do {
            randomSet.add(String.valueOf(RandomUtils.nextInt(ORDER_CODE_START_INCLUSIVE, ORDER_CODE_END_EXCLUSIVE)));
        } while (randomSet.size() < length);
        String randomNum = String.join("", randomSet.toArray(new String[0]));
        long offset = Long.parseLong(randomNum);
        Boolean usedOffset = operations.getBit(currentDate, offset);
        if (usedOffset != null && usedOffset) {
            return getRandom(currentDate, length);
        }
        operations.setBit(currentDate, offset, true);
        Long expire = stringRedisTemplate.getExpire(currentDate);
        if (expire == null || expire <= 0) {
            stringRedisTemplate.expire(currentDate, ORDER_CODE_KEY_TIMEOUT, TimeUnit.MINUTES);
        }
        return randomNum;
    }

}
