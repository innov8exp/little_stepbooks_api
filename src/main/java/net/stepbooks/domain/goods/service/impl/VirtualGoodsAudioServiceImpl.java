package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsAudioMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsAudioService;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VirtualGoodsAudioServiceImpl extends ServiceImpl<VirtualGoodsAudioMappter, VirtualGoodsAudioEntity>
        implements VirtualGoodsAudioService {
    @Override
    public void fillinAudio(VirtualGoodsDto virtualGoodsDto) {
        String goodsId = virtualGoodsDto.getId();
        LambdaQueryWrapper<VirtualGoodsAudioEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualGoodsAudioEntity::getGoodsId, goodsId);
        List<VirtualGoodsAudioEntity> audioList = list(wrapper);
        if (audioList != null && audioList.size() > 0) {
            virtualGoodsDto.setAudioList(audioList);
        }
    }
}
