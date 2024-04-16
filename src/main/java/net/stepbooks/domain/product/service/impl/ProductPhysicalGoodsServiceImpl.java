package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.domain.product.entity.ProductPhysicalGoods;
import net.stepbooks.domain.product.mapper.ProductPhysicalGoodsMapper;
import net.stepbooks.domain.product.service.ProductPhysicalGoodsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPhysicalGoodsServiceImpl extends ServiceImpl<ProductPhysicalGoodsMapper, ProductPhysicalGoods>
        implements ProductPhysicalGoodsService {

    private final PhysicalGoodsService physicalGoodsService;

    @Override
    public List<PhysicalGoodsEntity> getPhysicalGoodsListByProductId(String productId) {

        LambdaQueryWrapper<ProductPhysicalGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProductPhysicalGoods::getProductId, productId);
        List<ProductPhysicalGoods> productPhysicalGoodsList = list(wrapper);
        List<String> goodIds = productPhysicalGoodsList.stream().map(ProductPhysicalGoods::getGoodsId).toList();

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
