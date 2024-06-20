-- 增加活动签到支持

ALTER TABLE STEP_POINTS_RULE
    ADD COLUMN activity_start_day DATE,
    ADD COLUMN activity_end_day DATE;
