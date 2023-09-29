package com.stepbook.domain.ads.service.impl;

import com.stepbook.domain.ads.dto.AdvertisementDto;
import com.stepbook.domain.ads.entity.AdvertisementEntity;
import com.stepbook.domain.ads.service.AdvertisementService;
import com.stepbook.infrastructure.enums.AdsType;
import com.stepbook.infrastructure.mapper.AdvertisementMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementMapper advertisementMapper;

    public AdvertisementServiceImpl(AdvertisementMapper advertisementMapper) {
        this.advertisementMapper = advertisementMapper;
    }

    @Override
    public List<AdvertisementDto> listAllAdvertisements(AdsType adsType) {
        return advertisementMapper.findAdsDtos(adsType);
    }

    @Override
    public AdvertisementDto findAdsById(String id) {
        return advertisementMapper.findAdsDtoById(id);
    }

    @Override
    public AdvertisementEntity findAdvertisement(String id) {
        return advertisementMapper.selectById(id);
    }

    @Override
    public void createAdvertisement(AdvertisementEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        advertisementMapper.insert(entity);
    }

    @Override
    public void updateAdvertisement(String id, AdvertisementEntity updatedEntity) {
        AdvertisementEntity tagEntity = advertisementMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, tagEntity, "id", "modifiedAt");
        tagEntity.setModifiedAt(LocalDateTime.now());
        advertisementMapper.updateById(tagEntity);
    }

    @Override
    public void deleteAdvertisement(String id) {
        advertisementMapper.deleteById(id);
    }
}
