package co.botechservices.novlnovl.domain.payment.dto;

import lombok.Data;

@Data
public class AndroidVerifyReceiptResponse {

    private String kind;
    private String purchaseTimeMillis;
    // 0. Purchased
    // 1. Canceled
    // 2. Pending
    private Integer purchaseState;
    // 0. Yet to be consumed
    // 1. Consumed
    private Integer consumptionState;
    // 0. Test (i.e. purchased from a license testing account)
    // 1. Promo (i.e. purchased using a promo code)
    // 2. Rewarded (i.e. from watching a video ad instead of paying)
    private Integer purchaseType;
    // 0. Yet to be acknowledged
    // 1. Acknowledged
    private Integer acknowledgementState;
    private String developerPayload;
    private String orderId;
    private String purchaseToken;
    private String productId;
    private Integer quantity;
    private String obfuscatedExternalAccountId;
    private String obfuscatedExternalProfileId;
    private String regionCode;

}
