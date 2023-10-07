package com.stepbook.domain.advertisement.mapper;

import com.stepbook.interfaces.client.dto.AdvertisementDto;
import com.stepbook.domain.advertisement.entity.AdvertisementEntity;
import com.stepbook.infrastructure.enums.AdsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AdvertisementMapper extends BaseMapper<AdvertisementEntity> {

    List<AdvertisementDto> findAdsDtos(AdsType adsType);

    AdvertisementDto findAdsDtoById(String id);
}

