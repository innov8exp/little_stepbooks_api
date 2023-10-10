package net.stepbooks.domain.order.mapper;

import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.domain.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface OrderMapper extends BaseMapper<OrderEntity> {

    IPage<OrderInfoDto> findByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findPageByUser(Page<OrderInfoDto> page, String userId);
}
