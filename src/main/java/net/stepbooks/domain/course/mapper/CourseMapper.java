package net.stepbooks.domain.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.course.entity.Course;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {
    List<Course> findCoursesByProductId(String productId);
}
