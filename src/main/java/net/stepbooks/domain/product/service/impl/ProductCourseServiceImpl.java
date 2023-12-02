package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.product.entity.ProductCourse;
import net.stepbooks.domain.product.mapper.ProductCourseMapper;
import net.stepbooks.domain.product.service.ProductCourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCourseServiceImpl extends ServiceImpl<ProductCourseMapper, ProductCourse>
    implements ProductCourseService {

    @Override
    public List<ProductCourse> getProductCoursesByCourseId(String courseId) {
        return list(Wrappers.<ProductCourse>lambdaQuery().eq(ProductCourse::getCourseId, courseId));
    }
}
