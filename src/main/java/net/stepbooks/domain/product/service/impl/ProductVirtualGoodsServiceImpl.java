package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.entity.VirtualGoodsVideoEntity;
import net.stepbooks.domain.goods.service.VirtualGoodsAudioService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.domain.goods.service.VirtualGoodsVideoService;
import net.stepbooks.domain.product.entity.ProductVirtualGoods;
import net.stepbooks.domain.product.mapper.ProductVirtualGoodsMapper;
import net.stepbooks.domain.product.service.ProductVirtualGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVirtualGoodsServiceImpl extends ServiceImpl<ProductVirtualGoodsMapper, ProductVirtualGoods>
        implements ProductVirtualGoodsService {

    private final VirtualGoodsService virtualGoodsService;
    private final VirtualGoodsAudioService virtualGoodsAudioService;
    private final VirtualGoodsVideoService virtualGoodsVideoService;

    private void fillinAudio(VirtualGoodsDto virtualGoodsDto) {
        String goodsId = virtualGoodsDto.getId();
        LambdaQueryWrapper<VirtualGoodsAudioEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualGoodsAudioEntity::getGoodsId, goodsId);
        List<VirtualGoodsAudioEntity> audioList = virtualGoodsAudioService.list(wrapper);
        if (audioList != null && audioList.size() > 0) {
            virtualGoodsDto.setAudioList(audioList);
        }
    }

    private void fillinVideo(VirtualGoodsDto virtualGoodsDto) {
        String goodsId = virtualGoodsDto.getId();
        LambdaQueryWrapper<VirtualGoodsVideoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualGoodsVideoEntity::getGoodsId, goodsId);
        List<VirtualGoodsVideoEntity> videoList = virtualGoodsVideoService.list(wrapper);
        if (videoList != null && videoList.size() > 0) {
            virtualGoodsDto.setVideoList(videoList);
        }
    }

    @Override
    public List<VirtualGoodsDto> getVirtualGoodsListByProductId(String productId) {
        LambdaQueryWrapper<ProductVirtualGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProductVirtualGoods::getProductId, productId);
        List<ProductVirtualGoods> productPhysicalGoodsList = list(wrapper);
        List<String> goodIds = productPhysicalGoodsList.stream().map(ProductVirtualGoods::getGoodsId).toList();

        List<VirtualGoodsDto> results = new ArrayList<>();
        if (goodIds.size() > 0) {
            LambdaQueryWrapper<VirtualGoodsEntity> wrapper2 = Wrappers.lambdaQuery();
            wrapper2.in(VirtualGoodsEntity::getId, goodIds);
            List<VirtualGoodsEntity> virtualGoodsEntities = virtualGoodsService.list(wrapper2);
            for (VirtualGoodsEntity entity : virtualGoodsEntities) {
                VirtualGoodsDto virtualGoodsDto = BaseAssembler.convert(entity, VirtualGoodsDto.class);
                fillinAudio(virtualGoodsDto);
                fillinVideo(virtualGoodsDto);
                results.add(virtualGoodsDto);
            }
        }

        return results;
    }
}
