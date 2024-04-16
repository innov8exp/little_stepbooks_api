package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsAudioService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.domain.goods.service.VirtualGoodsVideoService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VirtualGoodsServiceImpl extends ServiceImpl<VirtualGoodsMappter, VirtualGoodsEntity>
        implements VirtualGoodsService {

    private final VirtualGoodsAudioService virtualGoodsAudioService;
    private final VirtualGoodsVideoService virtualGoodsVideoService;

    @Override
    public List<VirtualGoodsDto> listAll(String virtualCategoryId) {

        LambdaQueryWrapper<VirtualGoodsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualGoodsEntity::getCategoryId, virtualCategoryId);
        wrapper.orderByAsc(VirtualGoodsEntity::getSortIndex);

        List<VirtualGoodsEntity> virtualGoodsEntities = list(wrapper);

        List<VirtualGoodsDto> virtualGoodsDtos = new ArrayList<>();

        for (VirtualGoodsEntity entity : virtualGoodsEntities) {
            VirtualGoodsDto virtualGoodsDto = BaseAssembler.convert(entity, VirtualGoodsDto.class);
            virtualGoodsAudioService.fillinAudio(virtualGoodsDto);
            virtualGoodsVideoService.fillinVideo(virtualGoodsDto);
            virtualGoodsDtos.add(virtualGoodsDto);
        }

        return virtualGoodsDtos;
    }
}
