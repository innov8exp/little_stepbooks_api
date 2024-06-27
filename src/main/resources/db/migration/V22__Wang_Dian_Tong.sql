-- 旺店通支持，定时任务负责查找所有的已支付，但是尚未同步的订单，同步到旺店通

ALTER TABLE STEP_ORDER
    ADD COLUMN wdt_sync_status VARCHAR(32); -- 旺店通同步状态 INIT/NO_NEED/DONE
