package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.order.entity.OrderCourse;
import net.stepbooks.domain.order.mapper.OrderCourseMapper;
import net.stepbooks.domain.order.service.OrderCourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCourseServiceImpl extends ServiceImpl<OrderCourseMapper, OrderCourse> implements OrderCourseService {

    private final OrderCourseMapper orderCourseMapper;

    @Override
    public List<Course> findUserOrderCourses(String userId) {
        return orderCourseMapper.findUserOrderCourses(userId);
    }
}
