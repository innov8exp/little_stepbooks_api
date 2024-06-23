package net.stepbooks.domain.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.payment.config.WechatPayProperties;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.mapper.PaymentMapper;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.payment.vo.WechatPayPrePayRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundResponse;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    /**
     * 商户号
     */
    @Value("${wechat.merchant.id}")
    private String merchantId;
    /**
     * 商户API私钥路径
     */
    @Value("${wechat.merchant.private-key-path}")
    private String privateKeyPath;
    /**
     * 商户证书序列号
     */
    @Value("${wechat.merchant.serial-number}")
    private String merchantSerialNumber;
    /**
     * 商户APIV3密钥
     */
    @Value("${wechat.merchant.api-v3-key}")
    private String apiV3Key;
    @Value("${wechat.appId}")
    private String appId;

    @Resource
    private Config config;

    @Resource
    private WechatPayProperties wechatPayProperties;

    @Resource
    private JsapiServiceExtension jsapiServiceExtension;

    @Resource
    private RefundService refundService;

    @Resource
    private NotificationParser notificationParser;

    @Override
    public Payment getByOrder(String orderId) {
        LambdaQueryWrapper<Payment> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Payment::getOrderId, orderId);
        wrapper.eq(Payment::getPaymentType, PaymentType.ORDER_PAYMENT);
        Payment payment = getOne(wrapper);
        return payment;
    }

    @Override
    public PrepayWithRequestPaymentResponse prepayWithRequestPayment(WechatPayPrePayRequest createOrderPay) {
        log.info("prepayWithRequestPayment");
        PrepayRequest request = getPrepayRequest(createOrderPay);
        PrepayWithRequestPaymentResponse result;
        try {
            result = jsapiServiceExtension.prepayWithRequestPayment(request);
        } catch (HttpException e) {
            log.error("微信下单发送HTTP请求失败，错误信息：{}", e.getHttpRequest());
            throw new BusinessException(ErrorCode.PAYMENT_ERROR, "微信下单发送HTTP请求失败");
        } catch (ServiceException e) {
            // 服务返回状态小于200或大于等于300，例如500
            log.error("微信下单服务状态错误，错误信息：{}", e.getErrorMessage());
            throw new BusinessException(ErrorCode.PAYMENT_ERROR, "微信下单服务状态错误");
        } catch (MalformedMessageException e) {
            // 服务返回成功，返回体类型不合法，或者解析返回体失败
            log.error("服务返回成功，返回体类型不合法，或者解析返回体失败，错误信息：{}", e.getMessage());
            throw new BusinessException(ErrorCode.PAYMENT_ERROR, "服务返回成功，返回体类型不合法，或者解析返回体失败");
        }
        log.info("prepayWithRequestPayment end");
        return result;
    }

    @Override
    public Transaction queryStatus(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(wechatPayProperties.getMerchantId());
        request.setOutTradeNo(outTradeNo);
        try {
            return jsapiServiceExtension.queryOrderByOutTradeNo(request);
        } catch (ServiceException e) {
            log.error("订单查询失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new BusinessException(ErrorCode.PAYMENT_ERROR, "订单查询失败");
        }
    }

    @Override
    public void closeOrder(String outTradeNo) {
        log.info("closeOrder");
        CloseOrderRequest closeRequest = new CloseOrderRequest();
        closeRequest.setMchid(wechatPayProperties.getMerchantId());
        closeRequest.setOutTradeNo(outTradeNo);
        try {
            //方法没有返回值，意味着成功时API返回204 No Content
            jsapiServiceExtension.closeOrder(closeRequest);
        } catch (ServiceException e) {
            log.error("订单关闭失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new BusinessException(ErrorCode.PAYMENT_ERROR, "订单关闭失败");
        }
    }

    @Override
    public <T> T prePayNotify(RequestParam requestParam, Class<T> clazz) {
        log.debug("支付成功的回调。。。。。。。。。");
        log.info("prePayNotify");
        PrivacyEncryptor privacyEncryptor = config.createEncryptor();
        String weChatPayCertificateSerialNumber = privacyEncryptor.getWechatpaySerial();
        if (!requestParam.getSerialNumber().equals(weChatPayCertificateSerialNumber)) {
            log.error("证书不一致");
            throw new BusinessException(ErrorCode.PAYMENT_ERROR, "证书不一致");
        }
        return notificationParser.parse(requestParam, clazz);
    }

    @Override
    public WechatPayRefundResponse refund(WechatPayRefundRequest refundOrder) throws Exception {

        log.info("wechat refund start");
        try {
            //构建退款请求
            CreateRequest request = getRefundRequest(refundOrder);
            // 调用退款方法，得到应答
            // 调用微信sdk接口
            Refund refund = refundService.create(request);
            // 退款成功
            return BaseAssembler.convert(refund, WechatPayRefundResponse.class);
        } catch (ServiceException e) {
            log.error("退款失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new BusinessException(ErrorCode.PAYMENT_ERROR, "退款失败");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public <T> T refundNotify(RequestParam requestParam, Class<T> clazz) throws Exception {
        log.info("refundNotify");
        try {
            log.debug("支付成功的回调。。。。。。。。。");
            log.info("prePayNotify");
            PrivacyEncryptor privacyEncryptor = config.createEncryptor();
            String weChatPayCertificateSerialNumber = privacyEncryptor.getWechatpaySerial();
            if (!requestParam.getSerialNumber().equals(weChatPayCertificateSerialNumber)) {
                log.error("证书不一致");
                throw new BusinessException(ErrorCode.PAYMENT_ERROR, "证书不一致");
            }
            return notificationParser.parse(requestParam, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 构造退款请求体
     *
     * @param refundOrder 请求参数
     * @return PrepayRequest
     */
    private CreateRequest getRefundRequest(WechatPayRefundRequest refundOrder) {
        CreateRequest request = new CreateRequest();
        //构建订单金额信息
        AmountReq amountReq = new AmountReq();
        //退款金额
        amountReq.setRefund(refundOrder.getRefundMoney());
        //原订单金额
        amountReq.setTotal(refundOrder.getTotalMoney());
        //货币类型(默认人民币)
        amountReq.setCurrency("CNY");
        request.setAmount(amountReq);
        //商户退款单号
        request.setOutRefundNo(refundOrder.getOutRefundNo());
        request.setTransactionId(refundOrder.getTransactionId());
        request.setOutTradeNo(refundOrder.getOrderId());
        request.setReason(refundOrder.getReason());

        //退款通知回调地址
        request.setNotifyUrl(wechatPayProperties.getRefundNotifyUrl());
        return request;
    }

    /**
     * 构造预支付订单请求体
     *
     * @param createOrderPay 请求参数
     * @return PrepayRequest
     */
    private PrepayRequest getPrepayRequest(WechatPayPrePayRequest createOrderPay) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        BigDecimal payMoney = createOrderPay.getPayMoney();
        BigDecimal amountTotal = payMoney.multiply(new BigDecimal("100").setScale(0, RoundingMode.DOWN));
        amount.setTotal(amountTotal.intValue());
        request.setAmount(amount);
        Payer payer = new Payer();
        payer.setOpenid(createOrderPay.getOpenId());
        request.setPayer(payer);
        request.setTimeExpire(getExpiredTimeStr());
        request.setAppid(wechatPayProperties.getAppId());
        request.setMchid(wechatPayProperties.getMerchantId());
        request.setOutTradeNo(createOrderPay.getOutTradeNo());
//        request.setAttach(String.valueOf(createOrderPay.getOrderCode()));
        request.setDescription(createOrderPay.getPayContent());
        request.setNotifyUrl(wechatPayProperties.getPayNotifyUrl());
        //这里生成流水号，后续用这个流水号与微信交互，查询订单状态
        return request;
    }

    /**
     * 获取失效时间
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private String getExpiredTimeStr() {
        //失效时间，10分钟
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredTime = now.plusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return formatter.format(expiredTime);
    }
}
