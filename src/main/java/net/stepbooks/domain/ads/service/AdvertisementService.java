package net.stepbooks.domain.ads.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.infrastructure.enums.AdsType;
import net.stepbooks.interfaces.admin.dto.AdvertisementDto;

import java.util.List;

public interface AdvertisementService extends IService<Advertisement> {

    List<AdvertisementDto> listAdvertisementsByType(AdsType adsType);

    List<AdvertisementDto> listAdvertisements();

    AdvertisementDto findAdsById(String id);

    Advertisement findAdvertisement(String id);

}
