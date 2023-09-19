package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.admin.order.dto.ConsumptionInfoDto;
import co.botechservices.novlnovl.domain.order.entity.ConsumptionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface ConsumptionMapper extends BaseMapper<ConsumptionEntity> {

    IPage<ConsumptionInfoDto> findByCriteria(Page<ConsumptionInfoDto> page, String bookName, String username, String consumeOrderNo);

    IPage<ConsumptionInfoDto> findPageByUser(Page<ConsumptionInfoDto> page, String userId);
}
