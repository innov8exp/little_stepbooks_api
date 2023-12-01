package net.stepbooks.domain.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.interfaces.client.dto.CourseDto;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {
    List<CourseDto> findCoursesByProductId(String productId);

    CourseDto getCourseAndMediaById(String courseId);
}
