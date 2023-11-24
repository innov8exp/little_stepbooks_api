package net.stepbooks.domain.course.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.enums.CourseNature;
import net.stepbooks.domain.course.mapper.CourseMapper;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.domain.media.service.impl.PrivateFileServiceImpl;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.client.dto.CourseDto;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final CourseMapper courseMapper;
    private final PrivateFileServiceImpl privateFileService;
    private final OrderOpsService orderOpsService;

    @Override
    public List<Course> getBookCourses(String bookId) {
        return list(Wrappers.<Course>lambdaQuery().eq(Course::getBookId, bookId).orderByDesc(Course::getModifiedAt));
    }

    @Override
    public List<Course> findCoursesByProductId(String productId) {
        return courseMapper.findCoursesByProductId(productId);
    }

    @Override
    public String getTrialCourseUrl(String courseId) {
        CourseDto courseDto = courseMapper.getCourseAndMediaById(courseId);
        if (ObjectUtils.isEmpty(courseDto)) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (CourseNature.NEED_TO_PAY.equals(courseDto.getCourseNature())) {
            throw new BusinessException(ErrorCode.COURSE_NEED_TO_PAY);
        }
        return privateFileService.getUrl(courseDto.getVideoObjectKey());
    }

    @Override
    public String getCourseUrl(String userId, String courseId) {
        // check the authority of the user
        boolean exists = orderOpsService.checkCourseInUserOrder(userId, courseId);
        if (!exists) {
            throw new BusinessException(ErrorCode.COURSE_NEED_TO_PAY);
        }
        CourseDto courseDto = courseMapper.getCourseAndMediaById(courseId);
        return privateFileService.getUrl(courseDto.getVideoObjectKey());
    }

    @Override
    public List<Course> getBookCoursesByUser(String userId, String bookId) {
        boolean exists = orderOpsService.checkBookInUserOrder(userId, bookId);
        if (!exists) {
            throw new BusinessException(ErrorCode.BOOK_NOT_EXISTS_IN_ORDER_ERROR);
        }
        return list(Wrappers.<Course>lambdaQuery().eq(Course::getBookId, bookId));
    }

}
