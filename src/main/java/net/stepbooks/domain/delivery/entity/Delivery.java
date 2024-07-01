package net.stepbooks.domain.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.domain.delivery.enums.DeliveryMethod;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_DELIVERY")
public class Delivery extends BaseEntity {

    private String orderId;
    private String orderCode;
    private String userId;
    private String shipperUserId;
    private DeliveryMethod deliveryMethod;
    private DeliveryStatus deliveryStatus;
    private String deliveryCode;
    private String deliveryCompany;
    private String recipientName;
    private String recipientPhone;
    private String recipientProvince;
    private String recipientCity;
    private String recipientDistrict;
    private String recipientAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consignTime;
    private String logisticsId;
    private String logisticsType;
    private String logisticsName;
    private String logisticsNo;

}
