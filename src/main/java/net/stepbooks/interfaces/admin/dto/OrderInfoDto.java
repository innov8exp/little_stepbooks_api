package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.enums.WdtSyncStatus;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.model.BaseDto;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoDto extends BaseDto {

    private StoreType storeType;

    private String orderCode;
    private String userId;
    private String username;
    private String nickname;
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;
    private String productId;
    private String skuCode;
    private BigDecimal totalAmount;
    private OrderState state;
    private Boolean redeemed;
    private ProductNature productNature;
    private PaymentStatus paymentStatus;
    private WdtSyncStatus wdtSyncStatus;

    @Deprecated
    private List<OrderProductDto> products;
    /**
     * 使用skus替代products
     */
    private List<OrderSkuDto> skus;

    private Delivery delivery;
    private List<OrderEventLog> eventLogs;

    public String productName() {
        String productName = "";
        for (int i = 0; i < skus.size(); i++) {
            OrderSkuDto orderSkuDto = skus.get(i);
            productName = productName + orderSkuDto.getSkuName();
            if (i != skus.size() - 1) {
                productName = productName + ",";
            }
        }
        return productName;
    }
}
