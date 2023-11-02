package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.order.entity.OrderEntity;
import net.stepbooks.application.dto.admin.OrderInfoDto;

public interface OrderMapper extends BaseMapper<OrderEntity> {

    IPage<OrderInfoDto> findByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findPageByUser(Page<OrderInfoDto> page, String userId);
}
