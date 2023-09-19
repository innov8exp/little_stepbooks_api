package co.botechservices.novlnovl.domain.ads.service;


import co.botechservices.novlnovl.domain.ads.dto.AdvertisementDto;
import co.botechservices.novlnovl.domain.ads.entity.AdvertisementEntity;
import co.botechservices.novlnovl.infrastructure.enums.AdsType;

import java.util.List;

public interface AdvertisementService {

    List<AdvertisementDto> listAllAdvertisements(AdsType adsType);

    AdvertisementDto findAdsById(String id);

    AdvertisementEntity findAdvertisement(String id);

    void createAdvertisement(AdvertisementEntity entity);

    void updateAdvertisement(String id, AdvertisementEntity updatedEntity);

    void deleteAdvertisement(String id);
}
