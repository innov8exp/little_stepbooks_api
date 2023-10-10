package net.stepbooks.interfaces.client.controller.v1;

import net.stepbooks.interfaces.client.dto.OrderDto;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.product.entity.ProductEntity;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.domain.user.service.UserAccountService;
import net.stepbooks.infrastructure.enums.ClientPlatform;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.domain.payment.vo.*;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.payment.vo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ContextManager contextManager;
    private final PaymentService paymentService;
    private final UserAccountService userAccountService;
    private final ProductService productService;

    @PostMapping("/ios/verify")
    public ResponseEntity<Boolean> validateReceipt(@RequestBody IOSPaymentRequest ipr) {
        IOSVerifyReceiptRequest iosVerifyReceiptRequest = new IOSVerifyReceiptRequest();
        iosVerifyReceiptRequest.setReceiptData(ipr.getReceiptData());
        IOSVerifyReceiptResponse iosVerifyReceiptResponse = paymentService.verifyIOSPurchase(iosVerifyReceiptRequest);
        if (iosVerifyReceiptResponse.getStatus() == 0) {
            ProductEntity product = productService.findStoreProduct(ClientPlatform.IOS.getValue(), ipr.getStoreProductId());
            createOrder(product.getId());
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/android/verify")
    public ResponseEntity<Boolean> validateReceipt(@RequestBody AndroidPaymentRequest apr) {
        AndroidVerifyReceiptRequest androidVerifyReceiptRequest = new AndroidVerifyReceiptRequest();
        androidVerifyReceiptRequest.setToken(apr.getToken());
        androidVerifyReceiptRequest.setProductId(apr.getStoreProductId());
        AndroidVerifyReceiptResponse androidVerifyReceiptResponse = paymentService.verifyAndroidPurchase(androidVerifyReceiptRequest);
        if (androidVerifyReceiptResponse.getPurchaseState() == 0) {
            ProductEntity product = productService.findStoreProduct(ClientPlatform.ANDROID.getValue(), apr.getStoreProductId());
            createOrder(product.getId());
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.badRequest().body(false);
    }

    private void createOrder(String productId) {
        UserEntity currentUser = contextManager.currentUser();
        OrderDto orderDto = new OrderDto();
        orderDto.setProductId(productId);
        orderDto.setUserId(currentUser.getId());
        userAccountService.rechargeCoin(currentUser.getId(), orderDto);
    }
}
