package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsVideoEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsVideoMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsVideoService;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VirtualGoodsVideoServiceImpl extends ServiceImpl<VirtualGoodsVideoMappter, VirtualGoodsVideoEntity>
        implements VirtualGoodsVideoService {
    @Override
    public void fillinVideo(VirtualGoodsDto virtualGoodsDto) {
        String goodsId = virtualGoodsDto.getId();
        LambdaQueryWrapper<VirtualGoodsVideoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualGoodsVideoEntity::getGoodsId, goodsId);
        wrapper.orderByAsc(VirtualGoodsVideoEntity::getSortIndex);
        List<VirtualGoodsVideoEntity> videoList = list(wrapper);
        if (videoList != null && videoList.size() > 0) {
            virtualGoodsDto.setVideoList(videoList);
        }
    }
}
