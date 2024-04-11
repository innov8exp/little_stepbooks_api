package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsAudioMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsAudioService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VirtualGoodsAudioServiceImpl extends ServiceImpl<VirtualGoodsAudioMappter, VirtualGoodsAudioEntity>
        implements VirtualGoodsAudioService {
}
