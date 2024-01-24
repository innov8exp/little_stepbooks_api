package net.stepbooks.domain.pairedread.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.pairedread.entity.PairedReadCollectionUser;
import net.stepbooks.domain.pairedread.mapper.PairedReadCollectionUserMapper;
import net.stepbooks.domain.pairedread.service.PairedReadCollectionUserService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.PairedReadCollectionUserDto;
import net.stepbooks.interfaces.client.dto.PairedReadCollectionInfoDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PairedReadCollectionUserServiceImpl
        extends ServiceImpl<PairedReadCollectionUserMapper, PairedReadCollectionUser> implements PairedReadCollectionUserService {

    @Override
    public void create(PairedReadCollectionUser entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setId(null);
        this.baseMapper.insert(entity);
    }

    @Override
    public void update(String id, PairedReadCollectionUser updatedEntity) {
        this.checkExists(id);
        PairedReadCollectionUser tagEntity = this.baseMapper.selectById(id);
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
    public PairedReadCollectionUser getById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public IPage<PairedReadCollectionUser> getPage(Page<PairedReadCollectionUser> page, PairedReadCollectionUserDto queryDto) {
        String username = queryDto.getUsername();
        LambdaQueryWrapper<PairedReadCollectionUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(username), PairedReadCollectionUser::getUsername, username);
        return this.baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<PairedReadCollectionInfoDto> getAllByUsername(String username) {
        return this.baseMapper.selectCollectionByUsername(username);
    }

    private void checkExists(String id) {
        boolean exists = this.baseMapper.exists(
                Wrappers.<PairedReadCollectionUser>lambdaQuery().eq(PairedReadCollectionUser::getId, id));
        if (!exists) {
            throw new BusinessException(ErrorCode.PAIRED_READ_USER_NOT_EXISTS_ERROR);

        }

    }
}
