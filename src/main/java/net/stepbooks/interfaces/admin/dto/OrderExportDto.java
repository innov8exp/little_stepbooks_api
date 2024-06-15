package net.stepbooks.interfaces.admin.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderExportDto {

    static final int POS_0 = 0;
    static final int POS_1 = 1;
    static final int POS_2 = 2;
    static final int POS_3 = 3;
    static final int POS_4 = 4;
    static final int POS_5 = 5;
    static final int POS_6 = 6;
    static final int POS_7 = 7;
    static final int POS_8 = 8;
    static final int POS_9 = 9;
    static final int POS_10 = 10;
    static final int POS_11 = 11;
    static final int POS_12 = 12;
    static final int POS_13 = 13;

    @CsvBindByName(column = "订单号")
    @CsvBindByPosition(position = POS_0)
    private String orderCode;

    @CsvBindByName(column = "订单状态")
    @CsvBindByPosition(position = POS_1)
    private OrderState state;

    @CsvBindByName(column = "订单创建时间")
    @CsvBindByPosition(position = POS_2)
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @CsvBindByName(column = "收货人/提货人")
    @CsvBindByPosition(position = POS_3)
    private String recipientName;

    @CsvBindByName(column = "收货人手机号/提货人手机号")
    @CsvBindByPosition(position = POS_4)
    private String recipientPhone;

    @CsvBindByName(column = "收货人省份")
    @CsvBindByPosition(position = POS_5)
    private String recipientProvince;

    @CsvBindByName(column = "收货人城市")
    @CsvBindByPosition(position = POS_6)
    private String recipientCity;

    @CsvBindByName(column = "收货人地区")
    @CsvBindByPosition(position = POS_7)
    private String recipientDistrict;

    @CsvBindByName(column = "详细收货地址/提货地址")
    @CsvBindByPosition(position = POS_8)
    private String recipientAddress;

    @CsvBindByName(column = "交易类型")
    @CsvBindByPosition(position = POS_9)
    private PaymentType paymentType;

    @CsvBindByName(column = "交易方式")
    @CsvBindByPosition(position = POS_10)
    private PaymentMethod paymentMethod;

    @CsvBindByName(column = "交易时间")
    @CsvBindByPosition(position = POS_11)
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payAt;

    @CsvBindByName(column = "交易金额")
    @CsvBindByPosition(position = POS_12)
    private BigDecimal transactionAmount;

    @CsvBindByName(column = "交易流水号")
    @CsvBindByPosition(position = POS_12)
    private String vendorPaymentNo;

    @CsvBindByName(column = "交易状态")
    @CsvBindByPosition(position = POS_13)
    private String transactionStatus;

}
