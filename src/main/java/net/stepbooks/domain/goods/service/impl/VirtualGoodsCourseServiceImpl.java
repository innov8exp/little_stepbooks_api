package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsCourseEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsCourseMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsCourseService;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class VirtualGoodsCourseServiceImpl extends ServiceImpl<VirtualGoodsCourseMappter, VirtualGoodsCourseEntity>
        implements VirtualGoodsCourseService {
}
