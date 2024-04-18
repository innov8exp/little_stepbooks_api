package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.service.VirtualGoodsAudioService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.domain.goods.service.VirtualGoodsVideoService;
import net.stepbooks.domain.product.entity.SkuVirtualGoods;
import net.stepbooks.domain.product.mapper.SkuVirtualGoodsMapper;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuVirtualGoodsServiceImpl extends ServiceImpl<SkuVirtualGoodsMapper, SkuVirtualGoods>
        implements SkuVirtualGoodsService {

    private final VirtualGoodsService virtualGoodsService;
    private final VirtualGoodsAudioService virtualGoodsAudioService;
    private final VirtualGoodsVideoService virtualGoodsVideoService;

    @Override
    public List<VirtualGoodsDto> getVirtualGoodsListBySkuId(String skuId) {
        LambdaQueryWrapper<SkuVirtualGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SkuVirtualGoods::getSkuId, skuId);
        List<SkuVirtualGoods> skuPhysicalGoodsList = list(wrapper);
        List<String> goodIds = skuPhysicalGoodsList.stream().map(SkuVirtualGoods::getGoodsId).toList();

        List<VirtualGoodsDto> results = new ArrayList<>();
        if (goodIds.size() > 0) {
            LambdaQueryWrapper<VirtualGoodsEntity> wrapper2 = Wrappers.lambdaQuery();
            wrapper2.in(VirtualGoodsEntity::getId, goodIds);
            List<VirtualGoodsEntity> virtualGoodsEntities = virtualGoodsService.list(wrapper2);
            for (VirtualGoodsEntity entity : virtualGoodsEntities) {
                VirtualGoodsDto virtualGoodsDto = BaseAssembler.convert(entity, VirtualGoodsDto.class);
                virtualGoodsAudioService.fillinAudio(virtualGoodsDto);
                virtualGoodsVideoService.fillinVideo(virtualGoodsDto);
                results.add(virtualGoodsDto);
            }
        }

        return results;
    }
}
