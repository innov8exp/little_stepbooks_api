package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.Sku;
import net.stepbooks.domain.product.mapper.SkuMapper;
import net.stepbooks.domain.product.service.SkuService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku>
        implements SkuService {
}
