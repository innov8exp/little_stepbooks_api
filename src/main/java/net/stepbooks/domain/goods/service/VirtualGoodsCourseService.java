package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualGoodsCourseEntity;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;

public interface VirtualGoodsCourseService extends IService<VirtualGoodsCourseEntity> {
    void fillinCourse(VirtualGoodsDto virtualGoodsDto);
}
