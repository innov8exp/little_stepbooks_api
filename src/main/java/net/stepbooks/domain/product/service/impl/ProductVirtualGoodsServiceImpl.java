package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.ProductVirtualGoods;
import net.stepbooks.domain.product.mapper.ProductVirtualGoodsMapper;
import net.stepbooks.domain.product.service.ProductVirtualGoodsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductVirtualGoodsServiceImpl extends ServiceImpl<ProductVirtualGoodsMapper, ProductVirtualGoods>
        implements ProductVirtualGoodsService {
}
