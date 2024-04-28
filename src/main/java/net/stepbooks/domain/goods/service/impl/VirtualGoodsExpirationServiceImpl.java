package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsExpirationEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsExpirationMapper;
import net.stepbooks.domain.goods.service.VirtualGoodsExpirationService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VirtualGoodsExpirationServiceImpl extends ServiceImpl<VirtualGoodsExpirationMapper, VirtualGoodsExpirationEntity>
        implements VirtualGoodsExpirationService {

    @Override
    public List<VirtualGoodsExpirationEntity> validExpirations(String userId) {
        Date now = new Date();
        LambdaQueryWrapper<VirtualGoodsExpirationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.gt(VirtualGoodsExpirationEntity::getExpirationAt, now);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void redeem(String userId, String goodsId, String categoryId, int toAddMonth) {
        LambdaQueryWrapper<VirtualGoodsExpirationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(goodsId),
                VirtualGoodsExpirationEntity::getGoodsId, goodsId);
        wrapper.eq(ObjectUtils.isNotEmpty(categoryId),
                VirtualGoodsExpirationEntity::getCategoryId, categoryId);
        wrapper.eq(VirtualGoodsExpirationEntity::getUserId, userId);
        VirtualGoodsExpirationEntity expirationEntity = baseMapper.selectOne(wrapper);

        if (expirationEntity == null) {
            expirationEntity = new VirtualGoodsExpirationEntity();
            Calendar ca = Calendar.getInstance();
            int month = ca.get(Calendar.MONTH);
            ca.set(Calendar.MONTH, month + toAddMonth);
            Date failureTime = ca.getTime();
            Instant instant = failureTime.toInstant();
            LocalDateTime expirationAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            expirationEntity.setExpirationAt(expirationAt);
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
