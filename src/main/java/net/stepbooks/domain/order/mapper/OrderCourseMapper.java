package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.courses.course.entity.Course;
import net.stepbooks.domain.order.entity.OrderCourse;

import java.util.List;

public interface OrderCourseMapper extends BaseMapper<OrderCourse> {

    List<Course> findUserOrderCourses(String userId);
}
