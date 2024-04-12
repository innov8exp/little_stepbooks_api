package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.product.entity.ProductPhysicalGoods;

import java.util.List;

public interface ProductPhysicalGoodsService extends IService<ProductPhysicalGoods> {

    List<PhysicalGoodsEntity> getPhysicalGoodsListByProductId(String productId);
}
