
ALTER TABLE STEP_ORDER_INVENTORY_LOG ADD CHANGE_TYPE VARCHAR(20) DEFAULT 'DECREASE'; -- 变更类型: INCREASE 增加, DECREASE 减少