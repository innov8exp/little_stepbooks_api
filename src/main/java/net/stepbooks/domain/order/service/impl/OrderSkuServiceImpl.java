package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderSku;
import net.stepbooks.domain.order.mapper.OrderSkuMapper;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.product.entity.Sku;
import net.stepbooks.domain.product.service.SkuService;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSkuServiceImpl extends ServiceImpl<OrderSkuMapper, OrderSku>
        implements OrderSkuService {

    private final SkuService skuService;

    @Override
    public List<Sku> findSkusByOrderId(String orderId) {

        LambdaQueryWrapper<OrderSku> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OrderSku::getOrderId, orderId);
        List<OrderSku> orderSkus = list(wrapper);

        List<Sku> results = new ArrayList<>();
        for (OrderSku orderSku : orderSkus) {
            Sku sku = skuService.getById(orderSku.getSkuId());
            results.add(sku);
        }
        return results;
    }

    @Override
    public List<OrderSkuDto> findOrderSkusByOrderId(String orderId) {
        //TODO
        return null;
    }
}
