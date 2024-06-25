package net.stepbooks.interfaces;

/**
 * 放在Redis中的变量名
 */
public final class KeyConstants {


    private KeyConstants() {
    }

    /**
     * 订单正在导出的标记
     */
    public static final String FLAG_ORDER_EXPORT = "flagOrderExport";

    /**
     * 正在同步产品数据到旺店通
     */
    public static final String FLAG_WDT_SYNC = "flagWdtSync";

    /**
     * 最近一次同步商品到旺店通的操作时间
     */
    public static final String LAST_ACT_TIME_WDT_SYNC = "lastActTimeWdtSync";

    /**
     * 最新同步到旺店通的修改记录时间
     */
    public static final String LAST_MODIFY_AT_WDT_SYNC = "lastModifyAtWdtSync";

    /**
     * 订单任务正在运行的标记
     */
    public static final String FLAG_ORDER_JOB = "flagOrderJob";

}
