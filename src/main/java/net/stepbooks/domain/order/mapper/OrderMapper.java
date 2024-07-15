package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {

    IPage<OrderInfoDto> findByCriteria(StoreType storeType, Page<OrderInfoDto> page, String orderCode, String username,
                                       String state, LocalDateTime startDate, LocalDateTime endDate);

    IPage<OrderInfoDto> findPageByUser(StoreType storeType, Page<OrderInfoDto> page, String userId,
                                       OrderState state, String skuName);

    List<Product> findOrderProductByUserIdAndProductIds(String userId, List<String> productIds);
}
