package net.stepbooks.domain.coin.service;

import net.stepbooks.domain.coin.entity.CoinEntity;

import java.util.List;

public interface CoinService {

    List<CoinEntity> findCoinProducts();

    List<CoinEntity> findCoinProductsByPlatform(String platform);

    CoinEntity findCoinProduct(String id);

    CoinEntity findStoreCoinProduct(String platform, String storeProductId);

    void createCoinProduct(CoinEntity entity);

    void updateCoinProduct(String id, CoinEntity updatedEntity);

    void deleteCoinProduct(String id);
}
