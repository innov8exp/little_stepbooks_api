package net.stepbooks.domain.payment.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IOSVerifyReceiptRequest {
    @JsonProperty("receipt-data")
    private String receiptData;
    private String password;
    @JsonProperty("exclude-old-transactions")
    private String excludeOldTransactions;
}
