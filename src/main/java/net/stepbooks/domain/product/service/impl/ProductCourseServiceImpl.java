package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.product.entity.ProductCourse;
import net.stepbooks.domain.product.mapper.ProductCourseMapper;
import net.stepbooks.domain.product.service.ProductCourseService;
import org.springframework.stereotype.Service;

@Service
public class ProductCourseServiceImpl extends ServiceImpl<ProductCourseMapper, ProductCourse>
    implements ProductCourseService {
}
