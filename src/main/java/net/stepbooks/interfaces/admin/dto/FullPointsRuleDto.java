package net.stepbooks.interfaces.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.domain.product.entity.Product;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullPointsRuleDto {

    //每日签到积分
    private Integer dailyCheckInPoints;

    //连续3天签到积分，如果为空表示不支持
    private Integer threeDayCheckInPoints;

    //连续7天签到积分，如果为空表示不支持
    private Integer sevenDayCheckInPoints;

    //是否支持特殊节日签到
    private boolean specialCheckIn;

    //特殊节日签到积分
    private Integer specialCheckInPoints;

    //特殊节日签到开始日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activityStartDay;

    //特殊节日签到结束日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activityEndDay;

    //购买普通商品，每支付1元得到的积分奖励
    private Integer pointsPerYuanNormal;

    //是否支持积分促销
    private boolean pointsPromotion;

    //购买促销商品，每支付1元得到的积分奖励
    private Integer pointsPerYuanPromotion;

    //生效商品列表，多个ID以逗号分割，如果是"*"，表示全部商品
    private String spuIds;

    //商品列表展示信息，设置的时候不用传，冗余数据，展示用
    private List<Product> spus;

    //促销开始日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate promotionStartDay;

    //促销结束日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate promotionEndDay;

}
