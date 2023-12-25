package net.stepbooks.interfaces.client.controller.v1;

import com.wechat.pay.java.service.wexinpayscoreparking.model.Transaction;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.payment.vo.WechatPayPreNotifyRequest;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static net.stepbooks.infrastructure.AppConstants.ONE_HUNDRED;

@Slf4j
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class PaymentController {

    private final ContextManager contextManager;
    private final PaymentService paymentService;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;
    private final OrderOpsService orderOpsService;

    @PostMapping("/wechat/pay/callback")
    public ResponseEntity<Transaction> handleWechatPaymentNotify(@RequestBody WechatPayPreNotifyRequest preRequest) {
        log.info("start payment callback");
        Transaction transaction = paymentService.prePayNotify(preRequest, Transaction.class);
        log.info("transaction: {}", transaction);
        Order order = orderOpsService.findOrderByCode(transaction.getOutTradeNo());
        Payment payment = new Payment();
        payment.setVendorPaymentNo(transaction.getTransactionId());
        payment.setPaymentMethod(PaymentMethod.WECHAT_PAY);
        payment.setReceipt(transaction.getDescription());
        BigDecimal fen = new BigDecimal(transaction.getAmount().getTotal());
        payment.setTransactionAmount(fen.divide(new BigDecimal(ONE_HUNDRED)));
        payment.setTransactionStatus(transaction.getTradeState());
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.paymentCallback(order, payment);
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.paymentCallback(order, payment);
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/wechat/refund/callback")
    public ResponseEntity<Transaction> handleWechatRefundNotify(HttpServletRequest request) throws Exception {
        Transaction transaction = paymentService.refundNotify(request, Transaction.class);
        Order order = orderOpsService.findOrderByCode(transaction.getOutTradeNo());
        Payment payment = new Payment();
        payment.setVendorPaymentNo(transaction.getTransactionId());
        payment.setPaymentMethod(PaymentMethod.WECHAT_PAY);
        payment.setReceipt(transaction.getDescription());
        BigDecimal fen = new BigDecimal(transaction.getAmount().getTotal());
        payment.setTransactionAmount(fen.divide(new BigDecimal(ONE_HUNDRED)));
        payment.setTransactionStatus(transaction.getTradeState());
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.refundCallback(order, payment);
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.refundCallback(order, payment);
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok(transaction);
    }
}
