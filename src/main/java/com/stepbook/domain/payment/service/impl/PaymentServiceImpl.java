package com.stepbook.domain.payment.service.impl;

import com.stepbook.domain.payment.client.AndroidClient;
import com.stepbook.domain.payment.client.IOSClient;
import com.stepbook.domain.payment.dto.AndroidVerifyReceiptRequest;
import com.stepbook.domain.payment.dto.AndroidVerifyReceiptResponse;
import com.stepbook.domain.payment.dto.IOSVerifyReceiptRequest;
import com.stepbook.domain.payment.dto.IOSVerifyReceiptResponse;
import com.stepbook.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

    private final IOSClient iosClient;
    private final AndroidClient androidClient;

    @Override
    public IOSVerifyReceiptResponse verifyIOSPurchase(IOSVerifyReceiptRequest requestBody) {
        requestBody.setPassword(iosSharedSecret);
        return iosClient.verifyIOSPurchase(requestBody);
    }

    @Override
    public AndroidVerifyReceiptResponse verifyAndroidPurchase(AndroidVerifyReceiptRequest receiptRequest) {
        return androidClient.verifyAndroidPurchase(androidBundleId, receiptRequest.getProductId(),
                receiptRequest.getToken());
    }
}
