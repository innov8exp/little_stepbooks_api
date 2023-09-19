package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.admin.order.dto.OrderInfoDto;
import co.botechservices.novlnovl.domain.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface OrderMapper extends BaseMapper<OrderEntity> {

    IPage<OrderInfoDto> findByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findPageByUser(Page<OrderInfoDto> page, String userId);
}
