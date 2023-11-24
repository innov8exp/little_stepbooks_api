package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.product.entity.ProductBook;
import net.stepbooks.domain.product.mapper.ProductBookMapper;
import net.stepbooks.domain.product.service.ProductBookService;
import org.springframework.stereotype.Service;

@Service
public class ProductBookServiceImpl extends ServiceImpl<ProductBookMapper, ProductBook>
    implements ProductBookService {
}
