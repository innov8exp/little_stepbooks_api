package net.stepbooks.application.dto.client;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceOrderDto {

    private String skuNo;
    private String userName;
    private String phone;
    private String address;
    private BigDecimal transactionAmount;
}
