package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.wexinpayscoreparking.model.Transaction;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentOpsService;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.enums.TransactionStatus;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.wechat.pay.java.core.http.Constant.*;
import static net.stepbooks.infrastructure.AppConstants.ONE_HUNDRED;

@Slf4j
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class PaymentController {

    private final ContextManager contextManager;
    private final PaymentService paymentService;
    private final PaymentOpsService paymentOpsService;
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


    @PostMapping("/wechat/pay/callback")
    public ResponseEntity<Transaction> handleWechatPaymentNotify(@RequestBody String body,
                                                                 HttpServletRequest request) {
        RequestParam requestParam = buildRequestParam(body, request);
        log.info("callbackData: {}", body);
        log.info("callback header: {}", requestParam);
        log.info("start payment callback");
        Transaction transaction = paymentService.prePayNotify(requestParam, Transaction.class);
        log.info("transaction: {}", transaction);
        Order order = orderOpsService.findOrderByCode(transaction.getOutTradeNo());
        Payment payment = new Payment();
        payment.setVendorPaymentNo(transaction.getTransactionId());
        payment.setPaymentMethod(PaymentMethod.WECHAT_PAY);
        payment.setReceipt(transaction.getDescription());
        BigDecimal fen = new BigDecimal(transaction.getAmount().getTotal());
        payment.setTransactionAmount(fen.divide(new BigDecimal(ONE_HUNDRED)));
        payment.setTransactionStatus(transaction.getTradeState());
        correctOrderService(order).paymentCallback(order, payment);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/wechat/refund/callback")
    public ResponseEntity<Transaction> handleWechatRefundNotify(@RequestBody String body,
                                                                HttpServletRequest request) throws Exception {
        log.info("refund callbackData: {}", body);
        RequestParam requestParam = buildRequestParam(body, request);
        Transaction transaction = paymentService.refundNotify(requestParam, Transaction.class);
        Order order = orderOpsService.findOrderByCode(transaction.getOutTradeNo());
        Payment payment = paymentOpsService.getOne(Wrappers.<Payment>lambdaQuery()
                .eq(Payment::getOrderId, order.getId())
                .eq(Payment::getPaymentMethod, PaymentMethod.WECHAT_PAY)
                .eq(Payment::getPaymentType, PaymentType.REFUND_PAYMENT));
        payment.setPaymentMethod(PaymentMethod.WECHAT_PAY);
        payment.setPaymentType(PaymentType.REFUND_PAYMENT);
        payment.setReceipt(transaction.getDescription());
        BigDecimal fen = new BigDecimal(transaction.getAmount().getTotal());
        payment.setTransactionAmount(fen.divide(new BigDecimal(ONE_HUNDRED)));
        payment.setTransactionStatus(TransactionStatus.SUCCESS.name());
        correctOrderService(order).refundCallback(order, payment);
        return ResponseEntity.ok(transaction);
    }

    private static RequestParam buildRequestParam(String body, HttpServletRequest request) {
        String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
        String nonce = request.getHeader(WECHAT_PAY_NONCE);
        String signature = request.getHeader(WECHAT_PAY_SIGNATURE);
        String signatureType = request.getHeader("Wechatpay-Signature-Type");
        String serial = request.getHeader(WECHAT_PAY_SERIAL);
        return new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signType(signatureType)
                .signature(signature)
                .timestamp(timestamp)
                .body(body)
                .build();
    }
}
