--  积分任务
--  【重要】关于action_url的使用指南
--  用户在登录小程序后，需要第一时间获得当天的所有任务并解析其action_url，暂时需要实现以下4种任务
--  StepBook://local/virtualCategory?id=xxxxx 浏览某个具体虚拟产品大类详情页
--  StepBook://local/product?id=xxxxx 浏览某个具体的商品详情页
--  StepBook://local/dailyAudio 试听每日音频
--  StepBook://local/randomProduct 随机浏览商品
--  建议客户端缓存(action_url,task对象)，以便在任务完成的时候调用完成接口

create TABLE STEP_POINTS_TASK
(
    id           VARCHAR(100)                                     NOT NULL PRIMARY KEY,   -- 主键ID
    name         VARCHAR(100)                                     NOT NULL,               -- 任务名称
    type         VARCHAR(10) CHECK (type IN ('DAILY', 'SPECIAL')) NOT NULL,               -- 任务类型，每日任务或者特殊任务
    start_date   DATE,                                                                    -- 任务开始日期（每日任务该字段无效）
    end_date     DATE,                                                                    -- 任务结束日期（每日任务该字段无效）
    success_hint VARCHAR(100)                                     NOT NULL,               -- 完成积分任务时给用户的成功提醒
    points       INT                                              NOT NULL,               -- 完成任务增加的积分
    action_url   TEXT                                             NOT NULL,               -- 任务链接
    active       BOOLEAN                                          NOT NULL DEFAULT FALSE, -- 是否生效
    created_at   TIMESTAMP,
    modified_at  TIMESTAMP
);

--  积分任务完成记录
CREATE TABLE STEP_USER_POINTS_TASK
(
    id             VARCHAR(100)                                  NOT NULL PRIMARY KEY, -- 主键ID
    user_id        VARCHAR(100) REFERENCES STEP_USER (id)        NOT NULL,             -- 用户ID
    task_id        VARCHAR(100) REFERENCES STEP_POINTS_TASK (id) NOT NULL,             -- 任务ID
    points         INT                                           NOT NULL,             -- 完成任务增加的积分
    completed      BOOLEAN DEFAULT FALSE,                                              -- 是否完成
    completed_date DATE,                                                               -- 完成日期
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    UNIQUE (user_id, task_id, completed_date)
    -- 每日任务，每天只能完成一次，特殊任务需要程序逻辑判断保证不重复完成
);

-- 增加积分规则的生效spu范围，*表示全部spu，否则是以逗号分割的spuId列表
ALTER TABLE STEP_POINTS_RULE
    ADD COLUMN spus TEXT;

-- 积分日志表调整
ALTER TABLE STEP_USER_POINTS_LOG
    ADD COLUMN order_id VARCHAR(100), -- 订单ID
    ADD COLUMN status VARCHAR(20); -- 状态 PENDING/CONFIRMED/INVALID
