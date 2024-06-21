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
     * 订单任务正在运行的标记
     */
    public static final String FLAG_ORDER_JOB = "flagOrderJob";

}
