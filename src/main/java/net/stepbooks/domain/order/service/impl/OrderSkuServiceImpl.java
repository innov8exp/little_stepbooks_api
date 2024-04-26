package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderSku;
import net.stepbooks.domain.order.mapper.OrderSkuMapper;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.entity.Sku;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.product.service.SkuService;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSkuServiceImpl extends ServiceImpl<OrderSkuMapper, OrderSku>
        implements OrderSkuService {

    private final SkuService skuService;
    private final ProductService productService;

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
        LambdaQueryWrapper<OrderSku> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OrderSku::getOrderId, orderId);
        List<OrderSku> orderSkus = list(wrapper);
        List<OrderSkuDto> orderSkuDtos = new ArrayList<>();
        HashMap<String, Product> spuMap = new HashMap<>();
        for (OrderSku orderSku : orderSkus) {

            String spuId = orderSku.getSpuId();

            OrderSkuDto orderSkuDto = new OrderSkuDto();
            orderSkuDto.setOrderId(orderId);
            orderSkuDto.setQuantity(orderSku.getQuantity());

            Product product = spuMap.get(spuId);
            if (product == null) {
                product = productService.getById(spuId);
                spuMap.put(spuId, product);
            }

            orderSkuDto.setSpuId(spuId);
            orderSkuDto.setSpuName(product.getSkuName());
            orderSkuDto.setSpuCoverImgId(product.getCoverImgId());
            orderSkuDto.setSpuCoverImgUrl(product.getCoverImgUrl());
            orderSkuDto.setSpuDescription(product.getDescription());

            Sku sku = skuService.getById(orderSku.getSkuId());
            orderSkuDto.setSkuId(sku.getId());
            orderSkuDto.setSkuName(sku.getSkuName());
            orderSkuDto.setSkuPrice(sku.getPrice());

            orderSkuDtos.add(orderSkuDto);
        }

        return orderSkuDtos;
    }
}
