package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.product.entity.ProductMedia;
import net.stepbooks.interfaces.admin.dto.ProductMediaDto;

import java.util.List;

public interface ProductMediaService extends IService<ProductMedia> {

    List<ProductMediaDto> findMediasByProductId(String productId);
}
