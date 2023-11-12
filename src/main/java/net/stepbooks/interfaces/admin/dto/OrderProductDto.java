package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private String orderId;
    private String productId;
    private String skuCode;
    private String skuName;
    private BigDecimal price;
    private Boolean hasInventory;
    private String[] resources;
    private int quantity;
}
