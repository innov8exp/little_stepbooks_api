package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsVideoEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsVideoMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsVideoService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VirtualGoodsVideoServiceImpl extends ServiceImpl<VirtualGoodsVideoMappter, VirtualGoodsVideoEntity>
        implements VirtualGoodsVideoService {
}
