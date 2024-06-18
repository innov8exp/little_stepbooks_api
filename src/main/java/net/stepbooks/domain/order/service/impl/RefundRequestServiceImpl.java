package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.enums.RequestStatus;
import net.stepbooks.domain.order.mapper.RefundRequestMapper;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.RefundStatus;
import net.stepbooks.infrastructure.enums.RefundType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.RefundRequestDto;
import net.stepbooks.interfaces.client.dto.RefundRequestCreateDto;
import net.stepbooks.interfaces.client.dto.ReturnDeliveryInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundRequestServiceImpl extends ServiceImpl<RefundRequestMapper, RefundRequest>
        implements RefundRequestService {

    private final RefundRequestMapper refundRequestMapper;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;
    private final OrderService mixedOrderServiceImpl;
    private final OrderOpsService orderOpsService;

    private OrderService correctOrderService(Order order) {
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            return physicalOrderServiceImpl;
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            return virtualOrderServiceImpl;
        } else if (ProductNature.MIXED.equals(order.getProductNature())) {
            return mixedOrderServiceImpl;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundRequest createRefundRequest(RefundRequestCreateDto refundRequestCreateDto, String userId) {
        Order order = orderOpsService.findOrderByCode(refundRequestCreateDto.getOrderCode());
        // 只能申请自己的订单
        if (!userId.equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        // 只有已支付，已发货和完成的订单才能申请退款
        if (!OrderState.PAID.equals(order.getState())
                && !OrderState.SHIPPED.equals(order.getState())
                && !OrderState.FINISHED.equals(order.getState())) {
            throw new BusinessException(ErrorCode.ORDER_STATE_NOT_SUPPORT_REFUND);
        }
        RefundRequest refundRequest = BaseAssembler.convert(refundRequestCreateDto, RefundRequest.class);
        refundRequest.setUserId(userId);
        refundRequest.setRefundAmount(order.getTotalAmount());
        refundRequest.setOrderId(order.getId());
        refundRequest.setRefundStatus(RefundStatus.PENDING);
        refundRequest.setRequestStatus(RequestStatus.PENDING);
        if (OrderState.PAID.equals(order.getState())) {

//            refundRequest.setRequestStatus(RequestStatus.APPROVED);
//            refundRequest.setRefundType(RefundType.ONLY_REFUND);
//            try {
//                correctOrderService(order).refundRequest(order.getId(), refundRequest);
//            } catch (Exception e) {
//                throw new BusinessException(ErrorCode.REFUND_ERROR, e.getMessage());
//            }

            // 未发货的订单
            refundRequest.setRefundType(RefundType.ONLY_REFUND);

        } else {
            // 已发货的订单退货退款
            refundRequest.setRefundType(RefundType.REFUND_AND_RETURN);
        }
        save(refundRequest);
        return refundRequest;
    }

    @Override
    public List<RefundRequest> getRefundRequestsByOrderId(String orderId) {
        return list(Wrappers.<RefundRequest>lambdaQuery().eq(RefundRequest::getOrderId, orderId));
    }

    @Override
    public IPage<RefundRequestDto> getPagedRefundRequests(Page<RefundRequestDto> page, String orderCode) {
        return refundRequestMapper.getPagedRefundRequests(page, orderCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveRefundRequest(String id, BigDecimal refundAmount) {
        RefundRequest refundRequest = getById(id);
        String orderId = refundRequest.getOrderId();
        Order order = orderOpsService.findOrderById(orderId);
        correctOrderService(order).refundApprove(refundRequest.getOrderId(), refundAmount);
        refundRequest.setRequestStatus(RequestStatus.APPROVED);
        refundRequest.setRefundStatus(RefundStatus.REFUNDING_WAIT_DELIVERY);
        refundRequest.setRefundAmount(refundAmount);
        updateById(refundRequest);
    }

    @Override
    public void rejectRefundRequest(String id, String rejectReason) {
        RefundRequest refundRequest = getById(id);
        refundRequest.setRequestStatus(RequestStatus.REJECTED);
        refundRequest.setRefundStatus(RefundStatus.REFUND_FAILED);
        refundRequest.setRejectReason(rejectReason);
        updateById(refundRequest);
    }

    @Override
    public void fillDeliveryInfo(String id, ReturnDeliveryInfoDto deliveryInfo) {
        RefundRequest refundRequest = getById(id);
        refundRequest.setDeliveryCode(deliveryInfo.getDeliveryCode());
        refundRequest.setDeliveryCompany(deliveryInfo.getDeliveryCompany());
        refundRequest.setDeliveryStatus(DeliveryStatus.DELIVERING);
        refundRequest.setRefundStatus(RefundStatus.REFUNDING_WAIT_SIGN);
//        refundRequest.setRecipientName(deliveryInfo.getRecipientName());
//        refundRequest.setRecipientPhone(deliveryInfo.getRecipientPhone());
//        refundRequest.setRecipientLocation(deliveryInfo.getRecipientLocation());
//        refundRequest.setRecipientAddress(deliveryInfo.getRecipientAddress());
        updateById(refundRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signRefundDelivery(String id) {
        RefundRequest refundRequest = getById(id);
        refundRequest.setDeliveryStatus(DeliveryStatus.DELIVERED);
        refundRequest.setRefundStatus(RefundStatus.REFUNDING_WAIT_PAYMENT);
        updateById(refundRequest);
        try {
            Order order = orderOpsService.findOrderById(refundRequest.getOrderId());
            correctOrderService(order).refundPayment(order, refundRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.REFUND_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean existsRefundRequest(String orderCode) {
        return refundRequestMapper.checkRefundRequestExistsByOrderCode(orderCode);
    }

    @Override
    public RefundRequest getLatestRefundRequestByOrderId(String orderId) {
        return getOne(Wrappers.<RefundRequest>lambdaQuery().eq(RefundRequest::getOrderId, orderId)
                .orderByDesc(RefundRequest::getCreatedAt).last("limit 1"));
    }


    @Transactional
    protected void refundApprovedOrder(RefundRequest refundRequest) {
        log.info("refundApprovedOrder id={}", refundRequest.getOrderId());
    }

    @Override
    public void refundApprovedOrders() {
        LambdaQueryWrapper<RefundRequest> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RefundRequest::getRefundStatus, RefundStatus.REFUNDING_WAIT_DELIVERY);

        List<RefundRequest> refundRequests = list(wrapper);

        for (RefundRequest refundRequest : refundRequests) {
            refundApprovedOrder(refundRequest);
        }

    }


}
