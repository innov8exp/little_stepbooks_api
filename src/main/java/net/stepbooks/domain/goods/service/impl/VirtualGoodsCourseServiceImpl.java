package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsCourseEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsCourseMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsCourseService;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VirtualGoodsCourseServiceImpl extends ServiceImpl<VirtualGoodsCourseMappter, VirtualGoodsCourseEntity>
        implements VirtualGoodsCourseService {
    @Override
    public void fillinCourse(VirtualGoodsDto virtualGoodsDto) {
        String goodsId = virtualGoodsDto.getId();
        LambdaQueryWrapper<VirtualGoodsCourseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualGoodsCourseEntity::getGoodsId, goodsId);
        List<VirtualGoodsCourseEntity> courseList = list(wrapper);
        if (courseList != null && courseList.size() > 0) {
            virtualGoodsDto.setCourseList(courseList);
        }
    }
}
