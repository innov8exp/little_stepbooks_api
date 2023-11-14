package net.stepbooks.domain.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.product.entity.ProductMedia;

import java.util.List;

public interface ProductMediaMapper extends BaseMapper<ProductMedia> {

    List<Media> findMediasByProductId(String productId);
}
