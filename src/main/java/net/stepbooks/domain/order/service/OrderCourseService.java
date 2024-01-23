package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.courses.course.entity.Course;
import net.stepbooks.domain.order.entity.OrderCourse;

import java.util.List;

public interface OrderCourseService extends IService<OrderCourse> {

    List<Course> findUserOrderCourses(String userId);
}
