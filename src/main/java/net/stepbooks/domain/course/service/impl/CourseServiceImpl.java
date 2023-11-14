package net.stepbooks.domain.course.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.mapper.CourseMapper;
import net.stepbooks.domain.course.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public List<Course> getBookCourses(String bookId) {
        return list(Wrappers.<Course>lambdaQuery().eq(Course::getBookId, bookId));
    }
}
