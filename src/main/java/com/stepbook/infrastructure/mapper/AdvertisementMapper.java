package com.stepbook.infrastructure.mapper;

import com.stepbook.domain.ads.dto.AdvertisementDto;
import com.stepbook.domain.ads.entity.AdvertisementEntity;
import com.stepbook.infrastructure.enums.AdsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AdvertisementMapper extends BaseMapper<AdvertisementEntity> {

    List<AdvertisementDto> findAdsDtos(AdsType adsType);

    AdvertisementDto findAdsDtoById(String id);
}

