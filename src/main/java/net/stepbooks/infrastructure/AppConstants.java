package net.stepbooks.infrastructure;

public final class AppConstants {

    private AppConstants() {
    }

    public static final int MAX_PAGE_SIZE = 9999;

    /**
     * 虚拟商品增加的月份
     */
    public static final int TO_ADD_MONTH = 240;

    public static final int SEVEN_DAYS = 7;
    public static final int THREE_DAYS = 3;

    public static final String MIXED_ORDER_STATE_MACHINE_ID = "mixedOrderStateMachine";
    public static final String PHYSICAL_ORDER_STATE_MACHINE_ID = "physicalOrderStateMachine";
    public static final String VIRTUAL_ORDER_STATE_MACHINE_ID = "virtualOrderStateMachine";

    public static final String VIRTUAL_CATEGORY_ID_MEMBER = "1";

    public static final int DEFAULT_TO_ADD_MONTH = 240;

    public static final int ORDER_CODE_RANDOM_LENGTH = 5;
    public static final int ORDER_CODE_START_INCLUSIVE = 0;
    public static final int ORDER_CODE_END_EXCLUSIVE = 9;
    public static final int ORDER_CODE_KEY_TIMEOUT = 30;

    public static final String PHYSICAL_ORDER_CODE_PREFIX = "S";
    public static final String VIRTUAL_ORDER_CODE_PREFIX = "X";
    public static final String MIXED_ORDER_CODE_PREFIX = "M";

    public static final String ORDER_PAYMENT_TIMEOUT_QUEUE = "order-unpaid-timeout-queue";

    public static final long ORDER_PAYMENT_TIMEOUT = 30 * 60;
    public static final long ORDER_PAYMENT_TIMEOUT_BUFFER = 60;

    public static final int ONE_HUNDRED = 100;

}
