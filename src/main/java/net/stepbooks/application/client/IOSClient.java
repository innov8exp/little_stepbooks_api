package net.stepbooks.application.client;

import net.stepbooks.domain.payment.vo.IOSVerifyReceiptRequest;
import net.stepbooks.domain.payment.vo.IOSVerifyReceiptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "iOSClient", url = "${in-app-purchase.verify-host.ios}")
public interface IOSClient {

    @PostMapping(value = "/verifyReceipt")
    IOSVerifyReceiptResponse verifyIOSPurchase(@RequestBody IOSVerifyReceiptRequest requestBody);

}
