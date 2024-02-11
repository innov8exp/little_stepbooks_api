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
import net.stepbooks.domain.product.entity.ProductCourse;
import net.stepbooks.domain.product.service.ProductBookService;
import net.stepbooks.domain.product.service.ProductCourseService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.MCourseDto;
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
    private final ProductCourseService productCourseService;
    private final ProductBookService productBookService;

    @Override
    public List<Course> getBookCourses(String bookId) {
        return list(Wrappers.<Course>lambdaQuery().eq(Course::getBookId, bookId).orderByDesc(Course::getModifiedAt));
    }

    @Override
    public List<CourseDto> findCoursesByProductId(String productId) {
        return courseMapper.findCoursesByProductId(productId);
    }

    @Override
    public CourseDto getTrialCourseUrl(String courseId) {
        CourseDto courseDto = courseMapper.getCourseAndMediaById(courseId);
        if (ObjectUtils.isEmpty(courseDto)) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (CourseNature.NEED_TO_PAY.equals(courseDto.getCourseNature())) {
            throw new BusinessException(ErrorCode.COURSE_NEED_TO_PAY);
        }
        String url = privateFileService.getUrl(courseDto.getVideoObjectKey());
        courseDto.setVideoUrl(url);
        return courseDto;
    }

    @Override
    public CourseDto getCourseUrl(String userId, String courseId) {
        // check the authority of the user
        boolean exists = orderOpsService.checkCourseInUserOrder(userId, courseId);
        if (!exists) {
            throw new BusinessException(ErrorCode.COURSE_NEED_TO_PAY);
        }
        CourseDto courseDto = courseMapper.getCourseAndMediaById(courseId);
        String url = privateFileService.getUrl(courseDto.getVideoObjectKey());
        courseDto.setVideoUrl(url);
        return courseDto;
    }

    @Override
    public List<Course> getBookCoursesByUser(String userId, String bookId) {
        ////临时隐藏购买用户才能阅读的逻辑，将来再说
//        boolean exists = orderOpsService.checkBookInUserOrder(userId, bookId);
//        if (!exists) {
//            throw new BusinessException(ErrorCode.BOOK_NOT_EXISTS_IN_ORDER_ERROR);
//        }
        return list(Wrappers.<Course>lambdaQuery().eq(Course::getBookId, bookId));
    }

    @Override
    public MCourseDto getDetailById(String id) {
        Course course = getById(id);
        String videoObjectKey = course.getVideoUrl();
        String videoUrl = privateFileService.getUrl(videoObjectKey);
        course.setVideoUrl(videoUrl);
        MCourseDto courseDto = BaseAssembler.convert(course, MCourseDto.class);
        courseDto.setVideoKey(videoObjectKey);
        return courseDto;
    }

    @Override
    public void updateCourse(Course course) {
        updateById(course);
    }

    @Override
    public void createCourse(Course course) {
        save(course);
        String bookId = course.getBookId();
        List<ProductCourse> productCourses = productBookService.getProductBooksByBookId(bookId)
                .stream().map(productBook -> {
            String productId = productBook.getProductId();
            ProductCourse productCourse = new ProductCourse();
            productCourse.setBookId(bookId);
            productCourse.setCourseId(course.getId());
            productCourse.setProductId(productId);
            return productCourse;
        }).toList();
        productCourseService.saveBatch(productCourses);
    }

}
