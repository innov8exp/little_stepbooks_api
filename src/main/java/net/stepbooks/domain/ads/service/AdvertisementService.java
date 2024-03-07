package net.stepbooks.domain.ads.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.infrastructure.enums.AdsType;

import java.util.List;

public interface AdvertisementService extends IService<Advertisement> {

    List<Advertisement> listAdvertisements(AdsType adsType);

}
