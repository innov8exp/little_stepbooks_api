package co.botechservices.novlnovl.domain.payment.controller.v1;

import co.botechservices.novlnovl.domain.order.dto.OrderDto;
import co.botechservices.novlnovl.domain.payment.dto.*;
import co.botechservices.novlnovl.domain.payment.service.PaymentService;
import co.botechservices.novlnovl.domain.product.entity.ProductEntity;
import co.botechservices.novlnovl.domain.product.service.ProductService;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.domain.user.service.UserAccountService;
import co.botechservices.novlnovl.infrastructure.enums.ClientPlatform;
import co.botechservices.novlnovl.infrastructure.util.ContextManager;
import lombok.RequiredArgsConstructor;
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
