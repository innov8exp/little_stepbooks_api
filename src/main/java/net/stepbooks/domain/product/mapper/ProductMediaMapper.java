package net.stepbooks.domain.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.product.entity.ProductMedia;
import net.stepbooks.interfaces.admin.dto.ProductMediaDto;

import java.util.List;

public interface ProductMediaMapper extends BaseMapper<ProductMedia> {

    List<ProductMediaDto> findMediasByProductId(String productId);
}
