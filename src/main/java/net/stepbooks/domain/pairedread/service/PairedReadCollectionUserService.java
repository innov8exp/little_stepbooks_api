package net.stepbooks.domain.pairedread.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.pairedread.entity.PairedReadCollectionUser;
import net.stepbooks.interfaces.admin.dto.PairedReadCollectionUserDto;
import net.stepbooks.interfaces.client.dto.PairedReadCollectionInfoDto;

import java.util.List;

public interface PairedReadCollectionUserService {
    void create(PairedReadCollectionUser entity);

    void update(String id, PairedReadCollectionUser updatedEntity);

    void delete(String id);

    PairedReadCollectionUser getById(String id);

    IPage<PairedReadCollectionUser> getPage(Page<PairedReadCollectionUser> page, PairedReadCollectionUserDto queryDto);

    List<PairedReadCollectionInfoDto> getAllByUsername(String userId);
}
