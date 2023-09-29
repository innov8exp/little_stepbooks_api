package com.stepbook.domain.order.service.impl;

import com.stepbook.domain.admin.order.dto.ConsumptionInfoDto;
import com.stepbook.domain.order.entity.ConsumptionEntity;
import com.stepbook.domain.order.service.ConsumptionService;
import com.stepbook.infrastructure.mapper.ConsumptionMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsumptionServiceImpl implements ConsumptionService {

    private final ConsumptionMapper consumptionMapper;

    public ConsumptionServiceImpl(ConsumptionMapper consumptionMapper) {
        this.consumptionMapper = consumptionMapper;
    }

    @Override
    public IPage<ConsumptionInfoDto> findByCriteria(Page<ConsumptionInfoDto> page, String bookName, String username,
                                                    String orderNo) {
        return consumptionMapper.findByCriteria(page, bookName, username, orderNo);
    }

    @Override
    public IPage<ConsumptionInfoDto> findByUser(Page<ConsumptionInfoDto> page, String userId) {
        return consumptionMapper.findPageByUser(page, userId);
    }

    @Override
    public ConsumptionEntity findConsumption(String id) {
        return consumptionMapper.selectById(id);
    }

    @Override
    public List<ConsumptionEntity> findByBookAndUser(String bookId, String userId) {
        return consumptionMapper.selectList(Wrappers.<ConsumptionEntity>lambdaQuery()
                .eq(ConsumptionEntity::getBookId, bookId).eq(ConsumptionEntity::getUserId, userId));
    }

    @Override
    public void createConsumption(ConsumptionEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setConsumeOrderNo(IdWorker.getIdStr());
        consumptionMapper.insert(entity);
    }

    @Override
    public void updateConsumption(String id, ConsumptionEntity updatedEntity) {
        updatedEntity.setModifiedAt(LocalDateTime.now());
        ConsumptionEntity consumptionEntity = consumptionMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, consumptionEntity, "id", "createdAt");
        consumptionMapper.updateById(consumptionEntity);
    }

    @Override
    public void deleteConsumption(String id) {
        consumptionMapper.deleteById(id);
    }
}
