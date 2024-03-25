package net.stepbooks.domain.ads.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.domain.ads.mapper.AdvertisementMapper;
import net.stepbooks.domain.ads.service.AdvertisementService;
import net.stepbooks.infrastructure.enums.AdsType;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, Advertisement>
        implements AdvertisementService {

    private final AdvertisementMapper advertisementMapper;

    @Override
    public List<Advertisement> listAdvertisements(AdsType adsType) {
        LambdaQueryWrapper<Advertisement> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(adsType), Advertisement::getAdsType, adsType);
        wrapper.orderByDesc(Advertisement::getSortIndex);
        return baseMapper.selectList(wrapper);
    }

}
