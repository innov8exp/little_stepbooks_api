package net.stepbooks.domain.points.enums;

public enum PointsEventType {
    ACTIVITY_CHECK_IN, //特殊签到
    DAILY_CHECK_IN, //每日签到
    CHECK_IN_3_DAY, //连续3天签到
    CHECK_IN_7_DAY, //连续7天签到
    BUY_NORMAL_PRODUCT, //购买普通商品
    BUY_PROMOTION_PRODUCT, //购买促销商品
    FULL_PROMOTION, //全场促销对于非促销商品，如果有全场促销，则根据全场促销获得积分
}
