package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.mapper.VirtualGoodsMappter;
import net.stepbooks.domain.goods.service.*;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
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
    private final VirtualGoodsCourseService virtualGoodsCourseService;
    private final VirtualCategoryService virtualCategoryService;

    private void checkCategory(String categoryId) {
        if (virtualCategoryService.hasChild(categoryId)) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_CHILD);
        }
    }

    @Override
    public VirtualGoodsEntity create(VirtualGoodsEntity entity) {
        checkCategory(entity.getCategoryId());
        if (entity.getToAddMonth() == 0) {
            entity.setToAddMonth(AppConstants.DEFAULT_TO_ADD_MONTH);
        }
        save(entity);
        return entity;
    }

    @Override
    public VirtualGoodsEntity update(String id, VirtualGoodsEntity entity) {
        checkCategory(entity.getCategoryId());
        entity.setId(id);
        updateById(entity);
        return entity;
    }

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
            virtualGoodsCourseService.fillinCourse(virtualGoodsDto);
            virtualGoodsDtos.add(virtualGoodsDto);
        }

        return virtualGoodsDtos;
    }
}
