package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualGoodsVideoEntity;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;

public interface VirtualGoodsVideoService extends IService<VirtualGoodsVideoEntity> {

    void fillinVideo(VirtualGoodsDto virtualGoodsDto);
}
