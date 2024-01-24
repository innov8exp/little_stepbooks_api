package net.stepbooks.domain.pairedread.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.pairedread.entity.PairedRead;
import net.stepbooks.domain.pairedread.mapper.PairedReadMapper;
import net.stepbooks.domain.pairedread.service.PairedReadService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.PairedReadDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PairedReadServiceImpl extends ServiceImpl<PairedReadMapper, PairedRead> implements PairedReadService {

    @Override
    public void create(PairedRead entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setId(null);
        this.baseMapper.insert(entity);
    }

    @Override
    public void update(String id, PairedRead updatedEntity) {
        this.checkExists(id);
        PairedRead tagEntity = this.baseMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, tagEntity, "id", "modifiedAt");
        tagEntity.setModifiedAt(LocalDateTime.now());
        this.baseMapper.updateById(tagEntity);
    }

    @Override
    public void delete(String id) {
        this.checkExists(id);
        this.baseMapper.deleteById(id);
    }

    @Override
    public PairedRead getById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public IPage<PairedRead> getPage(Page<PairedRead> page, PairedReadDto queryDto) {
        String collectionId = queryDto.getCollectionId();
        String name = queryDto.getName();
        LambdaQueryWrapper<PairedRead> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(collectionId), PairedRead::getCollectionId, collectionId)
                .eq(ObjectUtils.isNotEmpty(name), PairedRead::getName, name);
        return this.baseMapper.selectPage(page, wrapper);
    }

    private void checkExists(String id) {
        boolean exists = this.baseMapper.exists(Wrappers.<PairedRead>lambdaQuery().eq(PairedRead::getId, id));
        if (!exists) {
            throw new BusinessException(ErrorCode.PAIRED_READ_NOT_EXISTS_ERROR);

        }

    }
}
