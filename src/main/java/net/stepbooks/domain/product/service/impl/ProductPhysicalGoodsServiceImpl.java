package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.ProductPhysicalGoods;
import net.stepbooks.domain.product.mapper.ProductPhysicalGoodsMapper;
import net.stepbooks.domain.product.service.ProductPhysicalGoodsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductPhysicalGoodsServiceImpl extends ServiceImpl<ProductPhysicalGoodsMapper, ProductPhysicalGoods>
        implements ProductPhysicalGoodsService {
}
