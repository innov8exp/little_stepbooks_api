package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.product.entity.ProductBook;
import net.stepbooks.domain.product.mapper.ProductBookMapper;
import net.stepbooks.domain.product.service.ProductBookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductBookServiceImpl extends ServiceImpl<ProductBookMapper, ProductBook>
    implements ProductBookService {

    @Override
    public List<ProductBook> getProductBooksByBookId(String bookId) {
        return list(Wrappers.<ProductBook>lambdaQuery().eq(ProductBook::getBookId, bookId));
    }
}
