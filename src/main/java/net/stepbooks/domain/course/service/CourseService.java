package net.stepbooks.domain.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.course.entity.Course;

import java.util.List;

public interface CourseService extends IService<Course> {

    List<Course> getBookCourses(String bookId);
}
