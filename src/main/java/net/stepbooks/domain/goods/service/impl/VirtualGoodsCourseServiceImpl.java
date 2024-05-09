package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.domain.goods.entity.VirtualGoodsCourseEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsCourseMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsCourseService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.CourseDto;
import net.stepbooks.interfaces.client.dto.VirtualGoodsCourseDto;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VirtualGoodsCourseServiceImpl extends ServiceImpl<VirtualGoodsCourseMappter, VirtualGoodsCourseEntity>
        implements VirtualGoodsCourseService {

    private final CourseService courseService;

    @Override
    public void fillinCourse(VirtualGoodsDto virtualGoodsDto) {
        String goodsId = virtualGoodsDto.getId();
        LambdaQueryWrapper<VirtualGoodsCourseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualGoodsCourseEntity::getGoodsId, goodsId);
        List<VirtualGoodsCourseEntity> courseList = list(wrapper);

        List<VirtualGoodsCourseDto> courseDtoList = new ArrayList<>();

        for (VirtualGoodsCourseEntity entity : courseList) {
            VirtualGoodsCourseDto dto = BaseAssembler.convert(entity, VirtualGoodsCourseDto.class);
            Course course = courseService.getById(dto.getCourseId());
            CourseDto courseDto = BaseAssembler.convert(course, CourseDto.class);
            dto.setCourse(courseDto);
            courseDtoList.add(dto);
        }

        virtualGoodsDto.setCourseList(courseDtoList);
    }
}
