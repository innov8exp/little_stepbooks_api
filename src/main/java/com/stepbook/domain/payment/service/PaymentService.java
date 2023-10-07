package com.stepbook.domain.payment.service;

import com.stepbook.domain.payment.vo.AndroidVerifyReceiptRequest;
import com.stepbook.domain.payment.vo.AndroidVerifyReceiptResponse;
import com.stepbook.domain.payment.vo.IOSVerifyReceiptRequest;
import com.stepbook.domain.payment.vo.IOSVerifyReceiptResponse;

public interface PaymentService {

    IOSVerifyReceiptResponse verifyIOSPurchase(IOSVerifyReceiptRequest receiptRequest);

    AndroidVerifyReceiptResponse verifyAndroidPurchase(AndroidVerifyReceiptRequest receiptRequest);
}
