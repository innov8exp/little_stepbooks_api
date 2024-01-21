package net.stepbooks.domain.pairedread.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.pairedread.entity.PairedRead;
import net.stepbooks.interfaces.admin.dto.PairedReadDto;

public interface PairedReadService {
    void create(PairedRead entity);

    void update(String id, PairedRead updatedEntity);

    void delete(String id);

    PairedRead getById(String id);

    IPage<PairedRead> getPage(Page<PairedRead> page, PairedReadDto queryDto);
}
