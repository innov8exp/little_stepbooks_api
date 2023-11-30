package net.stepbooks.domain.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.interfaces.admin.dto.MCourseDto;
import net.stepbooks.interfaces.client.dto.CourseDto;

import java.util.List;

public interface CourseService extends IService<Course> {

    List<Course> getBookCourses(String bookId);

    List<Course> findCoursesByProductId(String productId);

    CourseDto getTrialCourseUrl(String courseId);

    CourseDto getCourseUrl(String userId, String courseId);

    List<Course> getBookCoursesByUser(String userId, String bookId);

    MCourseDto getDetailById(String id);
}
