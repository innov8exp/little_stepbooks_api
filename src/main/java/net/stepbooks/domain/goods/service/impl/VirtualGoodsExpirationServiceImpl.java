package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsExpirationEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsExpirationMapper;
import net.stepbooks.domain.goods.service.VirtualGoodsExpirationService;
import org.springframework.stereotype.Service;

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
}
