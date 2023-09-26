package co.botechservices.novlnovl.domain.payment.client;

import co.botechservices.novlnovl.domain.payment.dto.IOSVerifyReceiptRequest;
import co.botechservices.novlnovl.domain.payment.dto.IOSVerifyReceiptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "iOSClient", url = "${in-app-purchase.verify-host.ios}")
public interface IOSClient {

    @PostMapping(value = "/verifyReceipt")
    IOSVerifyReceiptResponse verifyIOSPurchase(@RequestBody IOSVerifyReceiptRequest requestBody);

}
