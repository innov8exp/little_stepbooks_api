package net.stepbooks.domain.pairedread.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.pairedread.entity.PairedReadCollectionUser;
import net.stepbooks.interfaces.client.dto.PairedReadCollectionInfoDto;

import java.util.List;

public interface PairedReadCollectionUserMapper extends BaseMapper<PairedReadCollectionUser> {

    List<PairedReadCollectionInfoDto> selectCollectionByUserId(String userId);
}
