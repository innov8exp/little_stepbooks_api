package net.stepbooks.domain.advertisement.mapper;

import net.stepbooks.interfaces.client.dto.AdvertisementDto;
import net.stepbooks.domain.advertisement.entity.AdvertisementEntity;
import net.stepbooks.infrastructure.enums.AdsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AdvertisementMapper extends BaseMapper<AdvertisementEntity> {

    List<AdvertisementDto> findAdsDtos(AdsType adsType);

    AdvertisementDto findAdsDtoById(String id);
}

