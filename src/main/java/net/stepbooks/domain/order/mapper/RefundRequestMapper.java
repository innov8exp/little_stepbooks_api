package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.interfaces.admin.dto.RefundRequestDto;

public interface RefundRequestMapper extends BaseMapper<RefundRequest> {
    IPage<RefundRequestDto> getPagedRefundRequests(Page<RefundRequestDto> page, String orderCode);

    boolean checkRefundRequestExistsByOrderCode(String orderCode);
}
