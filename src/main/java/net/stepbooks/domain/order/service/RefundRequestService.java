package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.interfaces.client.dto.RefundRequestCreateDto;
import net.stepbooks.interfaces.client.dto.ReturnDeliveryInfoDto;

import java.math.BigDecimal;
import java.util.List;

public interface RefundRequestService extends IService<RefundRequest> {

    RefundRequest createRefundRequest(RefundRequestCreateDto refundRequestCreateDto, String userId);

    List<RefundRequest> getRefundRequestsByOrderId(String orderId);

    IPage<RefundRequest> getPagedRefundRequests(Page<RefundRequest> page, String orderCode);

    void approveRefundRequest(String id, BigDecimal refundAmount);

    void rejectRefundRequest(String id, String rejectReason);

    void fillDeliveryInfo(String id, ReturnDeliveryInfoDto deliveryInfo);

    void signRefundDelivery(String id);

    boolean existsRefundRequest(String orderCode);

    RefundRequest getLatestRefundRequestByOrderId(String orderId);
}
