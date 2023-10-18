package net.stepbooks.domain.coin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.stepbooks.domain.coin.entity.CoinEntity;
import net.stepbooks.domain.coin.mapper.CoinMapper;
import net.stepbooks.domain.coin.service.CoinService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    private final CoinMapper coinMapper;

    public CoinServiceImpl(CoinMapper coinMapper) {
        this.coinMapper = coinMapper;
    }


    @Override
    public List<CoinEntity> findCoinProducts() {
        return coinMapper.selectList(Wrappers.<CoinEntity>lambdaQuery()
                .orderByAsc(CoinEntity::getCoinAmount));
    }

    @Override
    public List<CoinEntity> findCoinProductsByPlatform(String platform) {
        return coinMapper.selectList(Wrappers.<CoinEntity>lambdaQuery()
                .eq(CoinEntity::getPlatform, platform)
                .orderByAsc(CoinEntity::getCoinAmount));
    }

    @Override
    public CoinEntity findCoinProduct(String id) {
        return coinMapper.selectById(id);
    }

    @Override
    public CoinEntity findStoreCoinProduct(String platform, String storeProductId) {
        return coinMapper.selectOne(Wrappers.<CoinEntity>lambdaQuery()
                .eq(CoinEntity::getPlatform, platform)
                .eq(CoinEntity::getStoreProductId, storeProductId)
        );
    }

    @Override
    public void createCoinProduct(CoinEntity entity) {
//        entity.setProductNo(IdWorker.getIdStr(entity));
        entity.setCoinNo(IdWorker.getIdStr());
        entity.setCreatedAt(LocalDateTime.now());
        coinMapper.insert(entity);
    }

    @Override
    public void updateCoinProduct(String id, CoinEntity updatedEntity) {
        updatedEntity.setId(id);
        coinMapper.updateById(updatedEntity);
    }

    @Override
    public void deleteCoinProduct(String id) {
        coinMapper.deleteById(id);
    }
}
