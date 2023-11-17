package net.stepbooks.domain.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.mapper.PaymentMapper;
import net.stepbooks.domain.payment.service.PaymentService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    /** 商户号 */
    private static final String MERCHANT_ID = "190000****";
    /** 商户API私钥路径 */
    private static final String PRIVATE_KEY_PATH = "/Users/yourname/your/path/apiclient_key.pem";
    /** 商户证书序列号 */
    private static final String MERCHANT_SERIAL_NUMBER = "5157F09EFDC096DE15EBE81A47057A72********";
    /** 商户APIV3密钥 */
    private static final String API_V_3_KEY = "...";


    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public void payment() {
        System.out.println("Wechat payment");

        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(MERCHANT_ID)
                        .privateKeyFromPath(PRIVATE_KEY_PATH)
                        .merchantSerialNumber(MERCHANT_SERIAL_NUMBER)
                        .apiV3Key(API_V_3_KEY)
                        .build();
        // 构建service
        JsapiService service = new JsapiService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid("wxa9d9651ae******");
        request.setMchid("190000****");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo("out_trade_no_001");
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getPrepayId());
    }
}
