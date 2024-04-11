package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsMappter;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VirtualGoodsServiceImpl extends ServiceImpl<VirtualGoodsMappter, VirtualGoodsEntity>
        implements VirtualGoodsService {
}
