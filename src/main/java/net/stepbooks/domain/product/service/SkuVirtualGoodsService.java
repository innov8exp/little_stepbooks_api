package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.product.entity.SkuVirtualGoods;
import net.stepbooks.domain.product.enums.RedeemCondition;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;

import java.util.List;

public interface SkuVirtualGoodsService extends IService<SkuVirtualGoods> {

    List<VirtualGoodsDto> getVirtualGoodsListBySkuId(String skuId, RedeemCondition redeemCondition);

}
