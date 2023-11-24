package net.stepbooks.domain.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.course.entity.Course;

import java.util.List;

public interface CourseService extends IService<Course> {

    List<Course> getBookCourses(String bookId);

    List<Course> findCoursesByProductId(String productId);

    String getTrialCourseUrl(String courseId);

    String getCourseUrl(String userId, String courseId);

    List<Course> getBookCoursesByUser(String userId, String bookId);
}
