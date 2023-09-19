package co.botechservices.novlnovl.domain.payment.service.impl;

import co.botechservices.novlnovl.domain.payment.dto.AndroidVerifyReceiptRequest;
import co.botechservices.novlnovl.domain.payment.dto.AndroidVerifyReceiptResponse;
import co.botechservices.novlnovl.domain.payment.dto.IOSVerifyReceiptRequest;
import co.botechservices.novlnovl.domain.payment.dto.IOSVerifyReceiptResponse;
import co.botechservices.novlnovl.domain.payment.service.PaymentService;
import co.botechservices.novlnovl.infrastructure.manager.webclient.WebClientBlockManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${in-app-purchase.verify-host.ios}")
    private String iosHost;
    @Value("${in-app-purchase.verify-host.android}")
    private String androidHost;
    @Value("${bundle-id.ios}")
    private String iosBundleId;
    @Value("${bundle-id.android}")
    private String androidBundleId;
    @Value("${in-app-purchase.shared-secret.ios}")
    private String iosSharedSecret;

    @Override
    public IOSVerifyReceiptResponse verifyIOSPurchase(IOSVerifyReceiptRequest requestBody) {
        WebClientBlockManager webClientBlockManager = WebClientBlockManager.build(iosHost);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/verifyReceipt");
        requestBody.setPassword(iosSharedSecret);
        return webClientBlockManager.post(uriComponentsBuilder.toUriString(), requestBody, IOSVerifyReceiptResponse.class);
    }

    @Override
    public AndroidVerifyReceiptResponse verifyAndroidPurchase(AndroidVerifyReceiptRequest receiptRequest) {
//        /androidpublisher/v3/applications/{packageName}/purchases/products/{productId}/tokens/{token}
        WebClientBlockManager webClientBlockManager = WebClientBlockManager.build(androidHost);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath("/androidpublisher/v3/applications/")
                .path(androidBundleId)
                .path("/purchases/products/")
                .path(receiptRequest.getProductId())
                .path("/tokens/")
                .path(receiptRequest.getToken());
        return webClientBlockManager.get(uriComponentsBuilder.toUriString(), AndroidVerifyReceiptResponse.class);
    }
}
