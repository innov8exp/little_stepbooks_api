package net.stepbooks.domain.pairedread.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.pairedread.entity.PairedReadCollection;
import net.stepbooks.domain.pairedread.enums.CollectionStatus;
import net.stepbooks.domain.pairedread.mapper.PairedReadCollectionMapper;
import net.stepbooks.domain.pairedread.service.PairedReadCollectionService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.PairedReadCollectionDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PairedReadCollectionServiceImpl
        extends ServiceImpl<PairedReadCollectionMapper, PairedReadCollection> implements PairedReadCollectionService {

    @Override
    public void create(PairedReadCollection entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setId(null);
        this.baseMapper.insert(entity);
    }

    @Override
    public void update(String id, PairedReadCollection updatedEntity) {
        this.checkExists(id);
        PairedReadCollection tagEntity = this.baseMapper.selectById(id);
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
    public PairedReadCollection getById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public IPage<PairedReadCollection> getPage(IPage<PairedReadCollection> page, PairedReadCollectionDto queryDto) {
        LambdaQueryWrapper<PairedReadCollection> wrapper = Wrappers.lambdaQuery();
        return this.baseMapper.selectPage(page, wrapper);
    }

    private void checkExists(String id) {
        boolean exists = this.baseMapper.exists(Wrappers.<PairedReadCollection>lambdaQuery().eq(PairedReadCollection::getId, id));
        if (!exists) {
            throw new BusinessException(ErrorCode.PAIRED_READ_COLLECTION_NOT_EXISTS_ERROR);

        }

    }

    @Override
    public List<PairedReadCollection> list() {
        LambdaQueryWrapper<PairedReadCollection> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PairedReadCollection::getStatus, CollectionStatus.ONLINE.name());
        return super.list(wrapper);
    }
}
