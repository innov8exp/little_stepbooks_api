package com.stepbook.domain.advertisement.service;


import com.stepbook.interfaces.client.dto.AdvertisementDto;
import com.stepbook.domain.advertisement.entity.AdvertisementEntity;
import com.stepbook.infrastructure.enums.AdsType;

import java.util.List;

public interface AdvertisementService {

    List<AdvertisementDto> listAllAdvertisements(AdsType adsType);

    AdvertisementDto findAdsById(String id);

    AdvertisementEntity findAdvertisement(String id);

    void createAdvertisement(AdvertisementEntity entity);

    void updateAdvertisement(String id, AdvertisementEntity updatedEntity);

    void deleteAdvertisement(String id);
}
