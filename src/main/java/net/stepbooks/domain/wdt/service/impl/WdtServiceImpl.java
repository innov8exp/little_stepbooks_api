package net.stepbooks.domain.wdt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.domain.wdt.service.WdtService;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.infrastructure.util.RedisStore;
import net.stepbooks.interfaces.KeyConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class WdtServiceImpl implements WdtService {

    private final PhysicalGoodsService physicalGoodsService;

    private final RedisDistributedLocker redisDistributedLocker;

    private final RedisStore redisStore;

    private final String jobName = "WdtGoodsSpecPush";

    /**
     * 启动后3分钟第一次执行
     */
    private final long initialDelay = 180000L;

    /**
     * 每15分钟执行一次
     */
    private final long fixedDelay = 900000L;

    private final long ten = 10L;

    /**
     * 一次性最多同步条数
     */
    private final long maxPageSize = 1000L;

    protected void goodsSpecPushImpl() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastActTime = redisStore.get(KeyConstants.LAST_ACT_TIME_WDT_SYNC, LocalDateTime.class);

        if (lastActTime != null && lastActTime.plusMinutes(ten).isAfter(now)) {
            log.info("{} was executed just now, ignore it", jobName);
            return;
        }

        redisStore.set(KeyConstants.LAST_ACT_TIME_WDT_SYNC, now);

        LocalDateTime lastModifyAt = redisStore.get(KeyConstants.LAST_MODIFY_AT_WDT_SYNC, LocalDateTime.class);

        LambdaQueryWrapper<PhysicalGoodsEntity> wrapper = Wrappers.lambdaQuery();
        if (lastModifyAt != null) {
            wrapper.gt(PhysicalGoodsEntity::getModifiedAt, lastModifyAt);
        }
        wrapper.orderByAsc(PhysicalGoodsEntity::getModifiedAt);

        Page<PhysicalGoodsEntity> page = Page.of(1, maxPageSize);
        IPage<PhysicalGoodsEntity> physicalGoods = physicalGoodsService.page(page, wrapper);
        for (PhysicalGoodsEntity physicalGoodsEntity : physicalGoods.getRecords()) {
            //转换成旺店通的格式并同步...
            lastModifyAt = physicalGoodsEntity.getModifiedAt();
        }

        if (lastModifyAt != null) {
            redisStore.set(KeyConstants.LAST_MODIFY_AT_WDT_SYNC, lastModifyAt);
        }

    }

    @Scheduled(initialDelay = initialDelay, fixedDelay = fixedDelay)
    @Override
    public void goodsSpecPush() {
        boolean res = redisDistributedLocker.tryLock(jobName);
        if (!res) {
            log.info("{} Job already in progress", jobName);
            return;
        }
        try {
            log.info("{} Job start ...", jobName);
            if (!redisStore.exists(KeyConstants.FLAG_WDT_SYNC)) {
                redisStore.setWithTwoMinutesExpiration(KeyConstants.FLAG_WDT_SYNC, true);
                log.info("{} impl ...", jobName);
                goodsSpecPushImpl();
            } else {
                log.info("{} execute too frequently", jobName);
            }
        } finally {
            redisDistributedLocker.unlock(jobName);
        }
    }

}
