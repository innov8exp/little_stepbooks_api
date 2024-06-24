-- 增加活动签到支持以及规则生效开关

ALTER TABLE STEP_POINTS_RULE
    ADD COLUMN activity_start_day DATE,
    ADD COLUMN activity_end_day DATE,
    ADD COLUMN active BOOLEAN NOT NULL DEFAULT FALSE;
