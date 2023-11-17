package net.stepbooks.domain.ads.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.infrastructure.enums.AdsType;
import net.stepbooks.interfaces.admin.dto.AdvertisementDto;

import java.util.List;

public interface AdvertisementMapper extends BaseMapper<Advertisement> {

    List<AdvertisementDto> findAdsDtos(AdsType adsType);

    AdvertisementDto findAdsDtoById(String id);
}

