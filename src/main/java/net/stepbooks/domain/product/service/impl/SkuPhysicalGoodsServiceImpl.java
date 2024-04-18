package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.domain.product.entity.SkuPhysicalGoods;
import net.stepbooks.domain.product.mapper.SkuPhysicalGoodsMapper;
import net.stepbooks.domain.product.service.SkuPhysicalGoodsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuPhysicalGoodsServiceImpl extends ServiceImpl<SkuPhysicalGoodsMapper, SkuPhysicalGoods>
        implements SkuPhysicalGoodsService {

    private final PhysicalGoodsService physicalGoodsService;

    @Override
    public List<PhysicalGoodsEntity> getPhysicalGoodsListBySkuId(String skuId) {
        LambdaQueryWrapper<SkuPhysicalGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SkuPhysicalGoods::getSkuId, skuId);
        List<SkuPhysicalGoods> skuPhysicalGoodsList = list(wrapper);
        List<String> goodIds = skuPhysicalGoodsList.stream().map(SkuPhysicalGoods::getGoodsId).toList();

        if (goodIds.size() > 0) {
            LambdaQueryWrapper<PhysicalGoodsEntity> wrapper2 = Wrappers.lambdaQuery();
            wrapper2.in(PhysicalGoodsEntity::getId, goodIds);
            List<PhysicalGoodsEntity> physicalGoodsEntities = physicalGoodsService.list(wrapper2);
            return physicalGoodsEntities;
        } else {
            return new ArrayList<>();
        }

    }
}
