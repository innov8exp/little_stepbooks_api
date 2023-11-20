package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.product.entity.ProductClassification;
import net.stepbooks.domain.product.mapper.ProductClassificationMapper;
import net.stepbooks.domain.product.service.ProductClassificationService;
import org.springframework.stereotype.Service;

@Service
public class ProductClassificationServiceImpl extends ServiceImpl<ProductClassificationMapper, ProductClassification>
        implements ProductClassificationService {
}
