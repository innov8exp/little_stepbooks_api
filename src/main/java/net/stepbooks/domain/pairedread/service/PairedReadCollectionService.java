package net.stepbooks.domain.pairedread.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import net.stepbooks.domain.pairedread.entity.PairedReadCollection;
import net.stepbooks.interfaces.admin.dto.PairedReadCollectionDto;

import java.util.List;

public interface PairedReadCollectionService {
    void create(PairedReadCollection entity);

    void update(String id, PairedReadCollection updatedEntity);

    void delete(String id);

    PairedReadCollection getById(String id);

    IPage<PairedReadCollection> getPage(IPage<PairedReadCollection> page, PairedReadCollectionDto queryDto);

    List<PairedReadCollection> list();

}
