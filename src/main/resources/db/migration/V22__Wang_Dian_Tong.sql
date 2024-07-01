-- 旺店通支持，定时任务负责查找所有的已支付，但是尚未同步的订单，同步到旺店通

ALTER TABLE STEP_ORDER
    ADD COLUMN wdt_sync_status VARCHAR(32); -- 旺店通同步状态 INIT/NO_NEED/REJECTED/DONE

ALTER TABLE STEP_PHYSICAL_GOODS
    ADD COLUMN wdt_goods_no VARCHAR(200); -- 旺店通货品编码

ALTER TABLE STEP_DELIVERY
    ADD COLUMN consign_time TIMESTAMP,  -- 发货时间
    ADD COLUMN logistics_id VARCHAR(200), -- 物流id
    ADD COLUMN logistics_type VARCHAR(200), -- 物流方式
    ADD COLUMN logistics_name VARCHAR(200), -- 物流方式名称
    ADD COLUMN logistics_no VARCHAR(200); -- 物流单号
