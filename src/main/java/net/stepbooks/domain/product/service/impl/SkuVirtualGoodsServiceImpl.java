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
import net.stepbooks.domain.product.enums.RedeemCondition;
import net.stepbooks.domain.product.mapper.SkuVirtualGoodsMapper;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.apache.commons.lang3.ObjectUtils;
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

    private void fillin(List<VirtualGoodsDto> results, String goodsId) {
        VirtualGoodsEntity entity = virtualGoodsService.getById(goodsId);
        VirtualGoodsDto virtualGoodsDto = BaseAssembler.convert(entity, VirtualGoodsDto.class);
        virtualGoodsAudioService.fillinAudio(virtualGoodsDto);
        virtualGoodsVideoService.fillinVideo(virtualGoodsDto);
        results.add(virtualGoodsDto);
    }

    @Override
    public List<VirtualGoodsDto> getVirtualGoodsListBySkuId(String skuId, RedeemCondition redeemCondition) {
        LambdaQueryWrapper<SkuVirtualGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SkuVirtualGoods::getSkuId, skuId);

        //不传redeemCondition则返回全部，否则按照redeemCondition查询
        wrapper.eq(ObjectUtils.isNotEmpty(redeemCondition), SkuVirtualGoods::getRedeemCondition, redeemCondition);
        List<SkuVirtualGoods> skuVirtualGoodsList = list(wrapper);

        List<VirtualGoodsDto> results = new ArrayList<>();

        for (SkuVirtualGoods skuVirtualGoods : skuVirtualGoodsList) {
            if (skuVirtualGoods.getGoodsId() == null) {
                //表示整个虚拟大类都展示
                LambdaQueryWrapper<VirtualGoodsEntity> wrapper2 = Wrappers.lambdaQuery();
                wrapper2.eq(VirtualGoodsEntity::getCategoryId, skuVirtualGoods.getCategoryId());
                wrapper2.orderByAsc(VirtualGoodsEntity::getSortIndex);
                List<VirtualGoodsEntity> virtualGoodsEntities = virtualGoodsService.list(wrapper2);
                for (VirtualGoodsEntity entity : virtualGoodsEntities) {
                    fillin(results, entity.getId());
                }

            } else {
                fillin(results, skuVirtualGoods.getGoodsId());
            }
        }

        return results;
    }
}
