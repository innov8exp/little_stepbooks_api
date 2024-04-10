package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.mapper.VirtualCategoryMapper;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VirtualCategoryServiceImpl extends ServiceImpl<VirtualCategoryMapper, VirtualCategoryEntity>
        implements VirtualCategoryService {
}
