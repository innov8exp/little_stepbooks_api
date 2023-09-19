package co.botechservices.novlnovl.domain.payment.service;

import co.botechservices.novlnovl.domain.payment.dto.AndroidVerifyReceiptRequest;
import co.botechservices.novlnovl.domain.payment.dto.AndroidVerifyReceiptResponse;
import co.botechservices.novlnovl.domain.payment.dto.IOSVerifyReceiptRequest;
import co.botechservices.novlnovl.domain.payment.dto.IOSVerifyReceiptResponse;

public interface PaymentService {

    IOSVerifyReceiptResponse verifyIOSPurchase(IOSVerifyReceiptRequest receiptRequest);

    AndroidVerifyReceiptResponse verifyAndroidPurchase(AndroidVerifyReceiptRequest receiptRequest);
}
