package co.botechservices.novlnovl.domain.order.service;

import co.botechservices.novlnovl.domain.admin.order.dto.ConsumptionInfoDto;
import co.botechservices.novlnovl.domain.order.entity.ConsumptionEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ConsumptionService {

    IPage<ConsumptionInfoDto> findByCriteria(Page<ConsumptionInfoDto> page, String bookName, String username, String orderNo);

    IPage<ConsumptionInfoDto> findByUser(Page<ConsumptionInfoDto> page, String userId);

    ConsumptionEntity findConsumption(String id);

    List<ConsumptionEntity> findByBookAndUser(String bookId, String userId);

    void createConsumption(ConsumptionEntity entity);

    void updateConsumption(String id, ConsumptionEntity updatedEntity);

    void deleteConsumption(String id);

}
