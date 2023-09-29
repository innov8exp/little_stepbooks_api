package com.stepbook.domain.payment.service;

import com.stepbook.domain.payment.dto.AndroidVerifyReceiptRequest;
import com.stepbook.domain.payment.dto.AndroidVerifyReceiptResponse;
import com.stepbook.domain.payment.dto.IOSVerifyReceiptRequest;
import com.stepbook.domain.payment.dto.IOSVerifyReceiptResponse;

public interface PaymentService {

    IOSVerifyReceiptResponse verifyIOSPurchase(IOSVerifyReceiptRequest receiptRequest);

    AndroidVerifyReceiptResponse verifyAndroidPurchase(AndroidVerifyReceiptRequest receiptRequest);
}
