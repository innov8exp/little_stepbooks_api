package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.mapper.PhysicalGoodsMapper;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PhysicalGoodsServiceImpl extends ServiceImpl<PhysicalGoodsMapper, PhysicalGoodsEntity>
        implements PhysicalGoodsService {

}
