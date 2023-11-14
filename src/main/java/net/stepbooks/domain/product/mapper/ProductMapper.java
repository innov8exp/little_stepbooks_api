package net.stepbooks.domain.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.interfaces.admin.dto.ProductDto;

public interface ProductMapper extends BaseMapper<Product> {

    ProductDto findDetailById(String id);
}
