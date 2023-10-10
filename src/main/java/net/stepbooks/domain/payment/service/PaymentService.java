package net.stepbooks.domain.payment.service;

import net.stepbooks.domain.payment.vo.AndroidVerifyReceiptRequest;
import net.stepbooks.domain.payment.vo.AndroidVerifyReceiptResponse;
import net.stepbooks.domain.payment.vo.IOSVerifyReceiptRequest;
import net.stepbooks.domain.payment.vo.IOSVerifyReceiptResponse;

public interface PaymentService {

    IOSVerifyReceiptResponse verifyIOSPurchase(IOSVerifyReceiptRequest receiptRequest);

    AndroidVerifyReceiptResponse verifyAndroidPurchase(AndroidVerifyReceiptRequest receiptRequest);
}
