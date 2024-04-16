package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;

public interface VirtualGoodsAudioService extends IService<VirtualGoodsAudioEntity> {
    void fillinAudio(VirtualGoodsDto virtualGoodsDto);
}
