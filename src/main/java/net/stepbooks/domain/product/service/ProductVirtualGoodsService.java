package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.product.entity.ProductVirtualGoods;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;

import java.util.List;

public interface ProductVirtualGoodsService extends IService<ProductVirtualGoods> {

    List<VirtualGoodsDto> getVirtualGoodsListByProductId(String productId);

}
