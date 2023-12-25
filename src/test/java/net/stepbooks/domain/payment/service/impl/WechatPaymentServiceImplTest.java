package net.stepbooks.domain.payment.service.impl;

import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.payment.vo.WechatPayPrePayRequest;
//import org.springframework.boot.test.context.SpringBootTest;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

//@SpringBootTest
class WechatPaymentServiceImplTest {
//    @Resource
    private PaymentService paymentService;


//    @Test
    @SuppressWarnings("checkstyle:MagicNumber")
    void prepayWithRequestPayment() {
        WechatPayPrePayRequest createOrderRequest = new WechatPayPrePayRequest();
//        createOrderRequest.setId(1001L);
        createOrderRequest.setOutTradeNo("100000001");
        createOrderRequest.setOpenId("o0mCV6-PMibNScBhWZb2tOa4Whvg");
        createOrderRequest.setPayMoney(new BigDecimal("0.01"));
        createOrderRequest.setPayContent("支付测试");
        PrepayWithRequestPaymentResponse response = paymentService.prepayWithRequestPayment(createOrderRequest);
        System.out.println(response.toString());
    }

//    @Test
    void queryStatus() {
        Transaction result = paymentService.queryStatus("100000001");
        System.out.println(result.toString());
        if (Transaction.TradeStateEnum.SUCCESS.equals(result.getTradeState())) {
            System.out.println("支付成功");
        } else {
            System.out.println("支付失败");
        }
    }

//    @Test
    void closeOrder() {
        paymentService.closeOrder("100000001");
    }

}
