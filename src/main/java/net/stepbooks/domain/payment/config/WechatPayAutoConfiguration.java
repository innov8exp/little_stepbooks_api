package net.stepbooks.domain.payment.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.refund.RefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.*;

@Slf4j
@Configuration
public class WechatPayAutoConfiguration {

    @Autowired
    private WechatPayProperties properties;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 自动更新证书
     *
     * @return RSAAutoCertificateConfig
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @Bean
    public Config config() throws IOException {
        String path = properties.getCertPemPath();
        Resource resourceCert = resourceLoader.getResource(path);
        X509Certificate certificate = getCertificate(resourceCert.getInputStream());
        String merchantSerialNumber = certificate.getSerialNumber().toString(16).toUpperCase();
        log.info("==========证书序列号：{}，商户信息：{}", merchantSerialNumber, certificate.getSubjectX500Principal());
        String privatePath = properties.getPrivateKeyPath();
        Resource resourcePrivate = resourceLoader.getResource(privatePath);
        String privateKey = inputStreamToString(resourcePrivate.getInputStream());
        log.info("==========加载微信私钥配置:{}", privateKey);
        RSAAutoCertificateConfig config = new RSAAutoCertificateConfig.Builder()
                .merchantId(properties.getMerchantId())
                .privateKey(privateKey)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(properties.getApiV3key())
                .build();
        return config;
    }

    /**
     * 微信支付对象
     *
     * @param config Config
     * @return JsapiServiceExtension
     */
    @Bean
    public JsapiServiceExtension jsapiServiceExtension(Config config) {
        log.info("==========加载微信支付对象");
        return new JsapiServiceExtension.Builder().config(config).build();
    }
    /**
     * 微信支付对象
     *
     * @param config Config
     * @return RefundService
     */
    @Bean
    public RefundService refundService(Config config) {
        log.info("==========加载微信支付对象");
        return new RefundService.Builder().config(config).build();
    }

    /**
     * 微信回调对象
     *
     * @param config Config
     * @return NotificationParser
     */
    @Bean
    public NotificationParser notificationParser(Config config) {
        log.info("==========加载微信回调解析对象");
        return new NotificationParser((NotificationConfig) config);
    }

    /**
     * 读取私钥文件，将文件流读取成string
     *
     * @param inputStream 私钥文件
     * @return 私钥文件字符串
     */
    public String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    /**
     * 获取证书 将文件流转成证书文件
     *
     * @param inputStream 证书文件
     * @return {@link X509Certificate} 获取证书
     */
    public static X509Certificate getCertificate(InputStream inputStream) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            cert.checkValidity();
            return cert;
        } catch (CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        } catch (CertificateException e) {
            throw new RuntimeException("无效的证书", e);
        }
    }

}
