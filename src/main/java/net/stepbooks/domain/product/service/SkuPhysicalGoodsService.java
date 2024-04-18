package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.product.entity.SkuPhysicalGoods;

import java.util.List;

public interface SkuPhysicalGoodsService extends IService<SkuPhysicalGoods> {

    List<PhysicalGoodsEntity> getPhysicalGoodsListBySkuId(String skuId);

}
