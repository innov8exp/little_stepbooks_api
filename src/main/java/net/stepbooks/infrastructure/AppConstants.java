package net.stepbooks.infrastructure;

public final class AppConstants {

    private AppConstants() {
    }

    public static final String PHYSICAL_ORDER_STATE_MACHINE_ID = "physicalOrderStateMachine";
    public static final String VIRTUAL_ORDER_STATE_MACHINE_ID = "virtualOrderStateMachine";

    public static final int ORDER_CODE_RANDOM_LENGTH = 5;
    public static final int ORDER_CODE_START_INCLUSIVE = 0;
    public static final int ORDER_CODE_END_EXCLUSIVE = 9;
    public static final int ORDER_CODE_KEY_TIMEOUT = 30;

    public static final String PHYSICAL_ORDER_CODE_PREFIX = "S";
    public static final String VIRTUAL_ORDER_CODE_PREFIX = "X";

    public static final String ORDER_PAYMENT_TIMEOUT_QUEUE = "order-unpaid-timeout-queue";

    public static final long ORDER_PAYMENT_TIMEOUT = 30 * 60;
    public static final long ORDER_PAYMENT_TIMEOUT_BUFFER = 60;

}
