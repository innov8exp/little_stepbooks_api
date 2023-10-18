package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.order.entity.ConsumptionEntity;
import net.stepbooks.interfaces.admin.dto.ConsumptionInfoDto;

public interface ConsumptionMapper extends BaseMapper<ConsumptionEntity> {

    IPage<ConsumptionInfoDto> findByCriteria(Page<ConsumptionInfoDto> page, String bookName, String username, String consumeOrderNo);

    IPage<ConsumptionInfoDto> findPageByUser(Page<ConsumptionInfoDto> page, String userId);
}
