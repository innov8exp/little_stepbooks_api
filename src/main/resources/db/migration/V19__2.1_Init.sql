-- 给虚拟产品大类增加"标签"字段，只有parent_id为空的虚拟产品大类才可以设置"标签"
-- 音频\课程\活动\播客\训练营，支持用逗号分隔的多个标签
ALTER TABLE STEP_VIRTUAL_CATEGORY
    ADD COLUMN tags VARCHAR(100);

-- 用户签到
create TABLE STEP_USER_CHECKIN_LOG
(
    id            VARCHAR(100)                           NOT NULL PRIMARY KEY, -- 主键ID
    user_id       VARCHAR(100) REFERENCES STEP_USER (id) NOT NULL,             -- 用户ID
    checkin_date  DATE                                   NOT NULL UNIQUE,      -- 签到日期
    continues_day INT                                    NOT NULL,             -- 连续签到几天，如果昨天没有签到，那么为1
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户积分日志
create TABLE STEP_USER_POINTS_LOG
(
    id            VARCHAR(100)                           NOT NULL PRIMARY KEY, -- 主键ID
    user_id       VARCHAR(100) REFERENCES STEP_USER (id) NOT NULL,             -- 用户ID
    event_type    VARCHAR(50)                            NOT NULL,             -- 事件类型，例如DAILY_CHECK_IN，7_DAY_CHECK_IN，30_DAY_CHECK_IN，MALL_REDEEM
    points_change INT                                    NOT NULL,             -- 积分变化，正数为增加，负数为减少
    reason        VARCHAR(100)                           NOT NULL,             -- 原因，例如"每日签到"，"积分商城兑换"
    expire_at     DATE                                   NOT NULL,             -- 积分失效时间，按照每自然年失效，为第二年的1月1日
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户（有效）总积分，
-- 如果离积分失效时间不远了，可以对用户进行积分使用提醒
create TABLE STEP_USER_POINTS
(
    id          VARCHAR(100)                           NOT NULL PRIMARY KEY, -- 主键ID
    user_id     VARCHAR(100) REFERENCES STEP_USER (id) NOT NULL,             -- 用户ID
    points      INT                                    NOT NULL DEFAULT 0,   -- 用户当年积分余额
    expire_at   DATE                                   NOT NULL,             -- 积分失效时间，按照每自然年失效，为第二年的1月1日
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 积分获取规制设置
-- 如果某个活动期间希望提高签到积分，可直接将DAILY_CHECK_IN设置为50点
create TABLE STEP_POINTS_RULE
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY, -- 主键ID
    event_type      VARCHAR(50)  NOT NULL UNIQUE,      -- 事件类型，例如DAILY_CHECK_IN，7_DAY_CHECK_IN，30_DAY_CHECK_IN
    exclusive_types VARCHAR(50),                       -- 当本规则生效的时候，其他几个规则失效
    reason          VARCHAR(50),                       -- 产生积分时给用户的描述
    points          INT          NOT NULL DEFAULT 0,   -- 当事件触发时获得的积分增长
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 虚拟大类和商品的关联表
-- 如果用户某个虚拟大类没有订阅，那么会引导他浏览对应的商品导购页面
create TABLE STEP_VIRTUAL_CATEGORY_PRODUCT
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,                  -- 主键ID
    category_id VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id), -- 大类ID
    product_id  VARCHAR(100) REFERENCES STEP_PRODUCT (id),          -- 产品ID
    created_at  TIMESTAMP,
    modified_at TIMESTAMP,
    UNIQUE (category_id, product_id)
);

-- 每日音频
create TABLE STEP_DAILY_AUDIO
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,                     -- 主键ID
    day         DATE         NOT NULL,                                 -- 每日音频播放日期
    category_id VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id),    -- 虚拟产品大类ID
    goods_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id),       -- 虚拟产品小类ID
    audio_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS_AUDIO (id), -- 音频ID
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);
