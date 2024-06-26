package net.stepbooks.domain.wdt.service;

public interface WdtService {

    /**
     * 同步平台货品
     *
     * @param updateAll 是否全部重新同步
     */
    void goodsSpecPush(boolean updateAll);

}
