package net.stepbooks.domain.order.enums;

public enum WdtSyncStatus {
    INIT,               //未同步
    NO_NEED,            //不需要同步
    REJECTED,           //被旺店通拒绝，可能是地址不对
    DONE,               //同步完成
}
