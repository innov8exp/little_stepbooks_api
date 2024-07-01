package net.stepbooks.domain.wdt.service;

public interface WdtService {

    /**
     * 同步平台货品
     *
     * @param updateAll 是否全部重新同步
     */
    void goodsSpecPush(boolean updateAll);

    /**
     * 同步交易
     */
    void tradePush();

    /**
     * 尝试重新推送订单
     *
     * @param orderId
     */
    void retryTradePush(String orderId);

    /**
     * 同步物流信息
     */
    void logisticsSyncQuery();

}
