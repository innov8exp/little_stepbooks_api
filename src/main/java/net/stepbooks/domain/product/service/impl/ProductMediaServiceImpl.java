package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.ProductMedia;
import net.stepbooks.domain.product.mapper.ProductMediaMapper;
import net.stepbooks.domain.product.service.ProductMediaService;
import net.stepbooks.interfaces.admin.dto.ProductMediaDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMediaServiceImpl extends ServiceImpl<ProductMediaMapper, ProductMedia> implements ProductMediaService {

    private final ProductMediaMapper productMediaMapper;

    @Override
    public List<ProductMediaDto> findMediasByProductId(String productId) {
        return productMediaMapper.findMediasByProductId(productId);
    }
}
