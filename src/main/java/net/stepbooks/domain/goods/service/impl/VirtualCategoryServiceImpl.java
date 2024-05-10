package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.domain.goods.mapper.VirtualCategoryMapper;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.VirtualCategoryAdminDto;
import net.stepbooks.interfaces.client.dto.VirtualCategoryDto;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VirtualCategoryServiceImpl extends ServiceImpl<VirtualCategoryMapper, VirtualCategoryEntity>
        implements VirtualCategoryService {

    @Override
    public VirtualCategoryEntity create(VirtualCategoryEntity entity) {
        if (entity.getType() == null) {
            entity.setType(VirtualCategoryType.MEDIA);
        }
        entity.setStatus(PublishStatus.OFFLINE);

        if (entity.getParentId() != null) {
            VirtualCategoryEntity parent = getById(entity.getParentId());
            if (parent.getParentId() != null) {
                throw new BusinessException(ErrorCode.CATEGORY_TREE_EXCEEDING_2_LEVELS);
            }
        }

        save(entity);
        return entity;
    }

    @Override
    public VirtualCategoryEntity update(String id, VirtualCategoryEntity entity) {
        if (entity.getType() == null) {
            entity.setType(VirtualCategoryType.MEDIA);
        }
        entity.setStatus(PublishStatus.OFFLINE);

        if (entity.getParentId() != null) {
            VirtualCategoryEntity parent = getById(entity.getParentId());
            if (parent.getParentId() != null) {
                throw new BusinessException(ErrorCode.CATEGORY_TREE_EXCEEDING_2_LEVELS);
            }
        }

        entity.setId(id);
        updateById(entity);
        return entity;
    }

    @Override
    public VirtualCategoryDto getFullVirtualCategoryById(String categoryId) {

        VirtualCategoryEntity entity = getById(categoryId);
        VirtualCategoryDto dto = BaseAssembler.convert(entity, VirtualCategoryDto.class);

        LambdaQueryWrapper<VirtualCategoryEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryEntity::getStatus, PublishStatus.ONLINE);
        wrapper.eq(VirtualCategoryEntity::getParentId, categoryId);
        wrapper.orderByAsc(VirtualCategoryEntity::getSortIndex);
        List<VirtualCategoryEntity> childEntities = list(wrapper);

        List<VirtualCategoryDto> children = new ArrayList<>();
        for (VirtualCategoryEntity childEntity : childEntities) {
            VirtualCategoryDto child = BaseAssembler.convert(childEntity, VirtualCategoryDto.class);
            children.add(child);
        }
        dto.setChildren(children);

        return dto;
    }

    @Override
    public VirtualCategoryAdminDto getAdminVirtualCategoryById(String categoryId) {
        VirtualCategoryEntity entity = getById(categoryId);
        VirtualCategoryAdminDto dto = BaseAssembler.convert(entity, VirtualCategoryAdminDto.class);
        if (dto.getParentId() != null) {
            VirtualCategoryEntity parentEntity = getById(dto.getParentId());
            VirtualCategoryAdminDto parentDto = BaseAssembler.convert(parentEntity, VirtualCategoryAdminDto.class);
            dto.setParent(parentDto);
        }
        return dto;
    }

    private List<VirtualCategoryDto> getMediaVirtualCategories(boolean freeOnly) {

        List<VirtualCategoryDto> results = new ArrayList<>();

        LambdaQueryWrapper<VirtualCategoryEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryEntity::getStatus, PublishStatus.ONLINE);
        wrapper.orderByAsc(VirtualCategoryEntity::getSortIndex);

        List<VirtualCategoryEntity> allEntities = list(wrapper);

        HashMap<String, VirtualCategoryDto> dtoMap = new HashMap<>();

        List<VirtualCategoryDto> tempChildren = new ArrayList<>();

        //加上全部一级分类
        for (VirtualCategoryEntity entity : allEntities) {
            VirtualCategoryDto dto = BaseAssembler.convert(entity, VirtualCategoryDto.class);
            dtoMap.put(dto.getId(), dto);
            if (entity.getParentId() == null) {
                if (freeOnly && BooleanUtils.isNotTrue(entity.getFree())) {
                    continue;
                }
                results.add(dto);
            } else {
                //暂存，后面处理
                tempChildren.add(dto);
            }
        }

        //加上全部子分类
        for (VirtualCategoryDto childDto : tempChildren) {
            String parentId = childDto.getParentId();
            VirtualCategoryDto parent = dtoMap.get(parentId);

            if (parent.getChildren() == null) {
                parent.setChildren(new ArrayList<>());
            }
            parent.getChildren().add(childDto);
        }

        return results;
    }

    @Override
    public List<VirtualCategoryDto> getAllMediaVirtualCategories() {
        return getMediaVirtualCategories(false);
    }

    @Override
    public List<VirtualCategoryDto> getFreeMediaVirtualCategories() {
        return getMediaVirtualCategories(true);
    }

    @Override
    public boolean hasChild(String categoryId) {
        LambdaQueryWrapper<VirtualCategoryEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryEntity::getParentId, categoryId);
        return count(wrapper) > 0;
    }

    @Override
    public List<VirtualCategoryAdminDto> allOnlineEndpoints() {

        List<VirtualCategoryAdminDto> results = new ArrayList<>();

        LambdaQueryWrapper<VirtualCategoryEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryEntity::getStatus, PublishStatus.ONLINE);
        wrapper.orderByAsc(VirtualCategoryEntity::getSortIndex);

        List<VirtualCategoryEntity> allEntities = list(wrapper);

        //全部大类
        HashMap<String, VirtualCategoryAdminDto> dtoMap = new HashMap<>();

        //所有父大类
        HashSet<String> parentSet = new HashSet<>();

        for (VirtualCategoryEntity entity : allEntities) {
            VirtualCategoryAdminDto dto = BaseAssembler.convert(entity, VirtualCategoryAdminDto.class);
            dtoMap.put(dto.getId(), dto);
            if (dto.getParentId() != null) {
                parentSet.add(dto.getParentId());
            }
        }

        for (VirtualCategoryEntity entity : allEntities) {
            VirtualCategoryAdminDto dto = dtoMap.get(entity.getId());
            if (!parentSet.contains(entity.getId())) {
                //确认是最终节点
                if (dto.getParentId() != null) {
                    VirtualCategoryAdminDto parent = dtoMap.get(dto.getParentId());
                    //parent为空，说明可能是下线了，这种情况不返回
                    if (parent == null) {
                        continue;
                    }
                    dto.setParent(parent);
                }
                results.add(dto);
            }
        }

        return results;
    }

}
