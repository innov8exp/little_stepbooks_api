package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.MemberExpirationEntity;
import net.stepbooks.domain.goods.mapper.MemberExpirationMapper;
import net.stepbooks.domain.goods.service.MemberExpirationService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class MemberExpirationServiceImpl extends ServiceImpl<MemberExpirationMapper, MemberExpirationEntity>
        implements MemberExpirationService {

    @Override
    public MemberExpirationEntity getExpirationByUserId(String userId) {
        Date now = new Date();
        LambdaQueryWrapper<MemberExpirationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.gt(MemberExpirationEntity::getExpirationAt, now);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void redeem(String userId, int toAddMonth) {
        LambdaQueryWrapper<MemberExpirationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MemberExpirationEntity::getUserId, userId);
        MemberExpirationEntity expirationEntity = baseMapper.selectOne(wrapper);

        if (expirationEntity == null) {
            expirationEntity = new MemberExpirationEntity();
            expirationEntity.setUserId(userId);
            Calendar ca = Calendar.getInstance();
            int month = ca.get(Calendar.MONTH);
            ca.set(Calendar.MONTH, month + toAddMonth);
            Date failureTime = ca.getTime();
            Instant instant = failureTime.toInstant();
            LocalDateTime expirationAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            expirationEntity.setExpirationAt(expirationAt);
            expirationEntity.setActiveAt(LocalDateTime.now());
            expirationEntity.setCreatedAt(LocalDateTime.now());
            save(expirationEntity);
        } else {
            Calendar ca = Calendar.getInstance();
            long now = System.currentTimeMillis();
            long oldExpirationAt = expirationEntity.getExpirationAt().toInstant(ZoneOffset.UTC).toEpochMilli();
            if (now < oldExpirationAt) {
                LocalDateTime oldDateTime = expirationEntity.getExpirationAt();
                Instant instant = oldDateTime.toInstant(ZoneOffset.UTC);
                long milliseconds = instant.toEpochMilli();
                ca.setTimeInMillis(milliseconds);
            }
            int month = ca.get(Calendar.MONTH);
            ca.set(Calendar.MONTH, month + toAddMonth);
            Date failureTime = ca.getTime();
            Instant instant = failureTime.toInstant();
            LocalDateTime expirationAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            expirationEntity.setExpirationAt(expirationAt);
            expirationEntity.setModifiedAt(LocalDateTime.now());
            updateById(expirationEntity);
        }
    }
}
