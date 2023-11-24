package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.order.entity.OrderCourse;
import net.stepbooks.domain.order.mapper.OrderCourseMapper;
import net.stepbooks.domain.order.service.OrderCourseService;
import org.springframework.stereotype.Service;

@Service
public class OrderCourseServiceImpl extends ServiceImpl<OrderCourseMapper, OrderCourse> implements OrderCourseService {
}
