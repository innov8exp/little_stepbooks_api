package net.stepbooks.domain.advertisement.service;


import net.stepbooks.interfaces.client.dto.AdvertisementDto;
import net.stepbooks.domain.advertisement.entity.AdvertisementEntity;
import net.stepbooks.infrastructure.enums.AdsType;

import java.util.List;

public interface AdvertisementService {

    List<AdvertisementDto> listAllAdvertisements(AdsType adsType);

    AdvertisementDto findAdsById(String id);

    AdvertisementEntity findAdvertisement(String id);

    void createAdvertisement(AdvertisementEntity entity);

    void updateAdvertisement(String id, AdvertisementEntity updatedEntity);

    void deleteAdvertisement(String id);
}
