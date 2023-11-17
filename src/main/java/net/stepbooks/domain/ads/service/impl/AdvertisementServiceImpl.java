package net.stepbooks.domain.ads.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.ads.entity.Advertisement;
import net.stepbooks.domain.ads.mapper.AdvertisementMapper;
import net.stepbooks.domain.ads.service.AdvertisementService;
import net.stepbooks.infrastructure.enums.AdsType;
import net.stepbooks.interfaces.admin.dto.AdvertisementDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, Advertisement>
        implements AdvertisementService {

    private final AdvertisementMapper advertisementMapper;

    @Override
    public List<AdvertisementDto> listAdvertisementsByType(AdsType adsType) {
        return advertisementMapper.findAdsDtos(adsType);
    }

    @Override
    public List<AdvertisementDto> listAdvertisements() {
        return advertisementMapper.findAdsDtos(null);
    }

    @Override
    public AdvertisementDto findAdsById(String id) {
        return advertisementMapper.findAdsDtoById(id);
    }

    @Override
    public Advertisement findAdvertisement(String id) {
        return advertisementMapper.selectById(id);
    }

}
