package co.botechservices.novlnovl.domain.payment.client;

import co.botechservices.novlnovl.domain.payment.dto.AndroidVerifyReceiptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "androidClient", url = "${in-app-purchase.verify-host.android}")
public interface AndroidClient {

    @GetMapping(value = "/androidpublisher/v3/applications/{androidBundleId}/purchases/products/{productId}/tokens/{token}")
    AndroidVerifyReceiptResponse verifyAndroidPurchase(
            @PathVariable("androidBundleId") String androidBundleId,
            @PathVariable("productId") String productId,
            @PathVariable("token") String token
    );

}
