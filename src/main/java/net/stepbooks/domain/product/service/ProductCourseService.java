package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.product.entity.ProductCourse;

import java.util.List;

public interface ProductCourseService extends IService<ProductCourse> {

    List<ProductCourse> getProductCoursesByCourseId(String courseId);

}
