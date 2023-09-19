package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.ads.dto.AdvertisementDto;
import co.botechservices.novlnovl.domain.ads.entity.AdvertisementEntity;
import co.botechservices.novlnovl.infrastructure.enums.AdsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AdvertisementMapper extends BaseMapper<AdvertisementEntity> {

    List<AdvertisementDto> findAdsDtos(AdsType adsType);

    AdvertisementDto findAdsDtoById(String id);
}

