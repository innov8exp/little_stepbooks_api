package net.stepbooks.domain.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.*;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.payment.config.WechatPayProperties;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.mapper.PaymentMapper;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.payment.vo.WechatPayPrePayRequest;
import net.stepbooks.domain.payment.vo.WechatPayPreNotifyRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.wechat.pay.java.core.http.Constant.*;

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


    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public void payment() {
        System.out.println("Wechat payment");

        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3Key)
                        .build();
        // 构建service
        JsapiService service = new JsapiService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid(appId);
        request.setMchid(merchantId);
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo("out_trade_no_001");
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getPrepayId());
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
            throw new RuntimeException("微信下单发送HTTP请求失败", e);
        } catch (ServiceException e) {
            // 服务返回状态小于200或大于等于300，例如500
            log.error("微信下单服务状态错误，错误信息：{}", e.getErrorMessage());
            throw new RuntimeException("微信下单服务状态错误", e);
        } catch (MalformedMessageException e) {
            // 服务返回成功，返回体类型不合法，或者解析返回体失败
            log.error("服务返回成功，返回体类型不合法，或者解析返回体失败，错误信息：{}", e.getMessage());
            throw new RuntimeException("服务返回成功，返回体类型不合法，或者解析返回体失败", e);
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
            throw new RuntimeException("订单查询失败", e);
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
            throw new RuntimeException("订单关闭失败", e);
        }
    }

    @Override
    public <T> T prePayNotify(WechatPayPreNotifyRequest wechatPayCallBackRequest, Class<T> clazz) {
        log.info("prePayNotify");
        PrivacyEncryptor privacyEncryptor = config.createEncryptor();
        String weChatPayCertificateSerialNumber = privacyEncryptor.getWechatpaySerial();
        if (!wechatPayCallBackRequest.getSerial().equals(weChatPayCertificateSerialNumber)) {
            log.error("证书不一致");
            throw new RuntimeException("证书不一致");
        }
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(wechatPayCallBackRequest.getSerial())
                .nonce(wechatPayCallBackRequest.getNonce())
                .signType(wechatPayCallBackRequest.getSignatureType())
                .signature(wechatPayCallBackRequest.getSignature())
                .timestamp(wechatPayCallBackRequest.getTimestamp())
                .body(wechatPayCallBackRequest.getBody())
                .build();
        return notificationParser.parse(requestParam, clazz);
    }

    @Override
    public WechatPayRefundResponse refund(WechatPayRefundRequest refundOrder) throws Exception {
        log.info("refund");
        try {
            //构建退款请求
            CreateRequest request = getRefundRequest(refundOrder);
            // 调用退款方法，得到应答
            // 调用微信sdk接口
            Refund refund = refundService.create(request);
            //接收退款返回参数
            Refund refundResponse = new Refund();
            refundResponse.setStatus(refund.getStatus());
            if (refundResponse.getStatus().equals(Status.SUCCESS)) {
                //退款成功
                return new WechatPayRefundResponse();
            }
        } catch (ServiceException e) {
            log.error("退款失败，返回码：{},返回信息：{}", e.getErrorCode(), e.getErrorMessage());
            throw new RuntimeException("退款失败", e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public <T> T refundNotify(HttpServletRequest request, Class<T> clazz) throws Exception {
        log.info("refundNotify");
        try {
            //读取请求体的信息
            ServletInputStream inputStream = request.getInputStream();
            StringBuilder stringBuffer = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            //读取回调请求体
            while ((s = bufferedReader.readLine()) != null) {
                stringBuffer.append(s);
            }
            String s1 = stringBuffer.toString();
            String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
            String nonce = request.getHeader(WECHAT_PAY_NONCE);
            String signType = request.getHeader("Wechatpay-Signature-Type");
            String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
            String signature = request.getHeader(WECHAT_PAY_SIGNATURE);

            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serialNo)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    // 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
                    .signType(signType)
                    .body(s1)
                    .build();
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
        amountReq.setRefund(Long.valueOf(refundOrder.getRefundMoney()));
        //原订单金额
        amountReq.setTotal(Long.valueOf(refundOrder.getTotalMoney()));
        //货币类型(默认人民币)
        amountReq.setCurrency("CNY");
        request.setAmount(amountReq);
        //商户退款单号
        request.setOutRefundNo(String.valueOf(refundOrder.getOutRefundNo()));
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
        request.setAttach(String.valueOf(createOrderPay.getId()));
        request.setDescription(createOrderPay.getPayContent());
        request.setNotifyUrl(wechatPayProperties.getPayNotifyUrl());
        //这里生成流水号，后续用这个流水号与微信交互，查询订单状态
        request.setOutTradeNo(createOrderPay.getOutTradeNo());
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
