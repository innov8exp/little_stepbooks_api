-- 物理产品
create TABLE STEP_PHYSICAL_GOODS
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,       -- 主键ID
    name        TEXT         NOT NULL,                   -- 产品名称
    description TEXT,                                    -- 产品描述
    img_id      VARCHAR(100) REFERENCES STEP_MEDIA (id), -- 图片ID
    img_url     TEXT,                                    -- 图片链接
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 虚拟产品大类
create TABLE STEP_VIRTUAL_CATEGORY
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,       -- 主键ID
    name        TEXT         NOT NULL,                   -- 大类名称
    cover_id    VARCHAR(100) REFERENCES STEP_MEDIA (id), -- 封面图片ID
    cover_url   TEXT,                                    -- 封面图片链接
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 虚拟产品（小类）
create TABLE STEP_VIRTUAL_GOODS
(
    id           VARCHAR(100) NOT NULL PRIMARY KEY,                  -- 主键ID
    category_id  VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id), -- 大类ID
    name         TEXT         NOT NULL,                              -- 产品名称
    description  TEXT,                                               -- 产品描述
    to_add_month SMALLINT,                                           -- 增加几个月有效期，对于会员可以是1，3，12个月，对于其他虚拟产品，默认是12个月
    created_at   TIMESTAMP,
    modified_at  TIMESTAMP
);

-- 虚拟产品小类包含的音频
create TABLE STEP_VIRTUAL_GOODS_AUDIO
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,               -- 主键ID
    goods_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id), -- 虚拟产品小类ID
    name        TEXT         NOT NULL,                           -- 音频名称
    cover_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),         -- 封面图片ID
    cover_url   TEXT,                                            -- 封面图片链接
    audio_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),         -- 音频ID
    audio_url   TEXT,                                            -- 音频链接
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 虚拟产品小类包含的视频
create TABLE STEP_VIRTUAL_GOODS_VIDEO
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,               -- 主键ID
    goods_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id), -- 虚拟产品小类ID
    name        TEXT         NOT NULL,                           -- 视频名称
    cover_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),         -- 封面图片ID
    cover_url   TEXT,                                            -- 封面图片链接
    video_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),         -- 视频ID
    video_url   TEXT,                                            -- 视频链接
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 虚拟产品小类包含的课程（1.0版本的course）
create TABLE STEP_VIRTUAL_GOODS_COURSE
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,               -- 主键ID
    goods_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id), -- 虚拟产品小类ID
    course_id   VARCHAR(100) REFERENCES STEP_COURSE (id),        -- 课程ID（每个课程下面还有一些具体练习）
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 会员过期状态记录
create TABLE STEP_MEMBER_EXPIRATION
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,      -- 主键ID
    user_id       VARCHAR(100) REFERENCES STEP_USER (id), -- 用户ID
    active_at     TIMESTAMP,                              -- 最近一次激活时间
    expiration_at TIMESTAMP,                              -- 过期时间
    created_at    TIMESTAMP
);

-- 虚拟产品过期状态记录
create TABLE STEP_VIRTUAL_GOODS_EXPIRATION
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,               -- 主键ID
    user_id       VARCHAR(100) REFERENCES STEP_USER (id),          -- 用户ID
    goods_id      VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id), -- 虚拟商品ID
    active_at     TIMESTAMP,                                       -- 最近一次激活时间
    expiration_at TIMESTAMP,                                       -- 过期时间
    created_at    TIMESTAMP
);

-- SKU产品与物理产品关系
create TABLE STEP_PRODUCT_PHYSICAL_GOODS_REF
(
    id         VARCHAR(100)                                     NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT (id)        NOT NULL,
    goods_id   VARCHAR(100) REFERENCES STEP_PHYSICAL_GOODS (id) NOT NULL
);

-- SKU产品与虚拟产品关系
create TABLE STEP_PRODUCT_VIRTUAL_GOODS_REF
(
    id               VARCHAR(100)                                    NOT NULL PRIMARY KEY,
    redeem_condition VARCHAR(100), -- PAYMENT_SUCCESS (支付成功立即兑换), SIGN_SUCCESS (签收成功兑换)
    product_id       VARCHAR(100) REFERENCES STEP_PRODUCT (id)       NOT NULL,
    goods_id         VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id) NOT NULL
);

-- 订单与物理产品关系
create TABLE STEP_ORDER_PHYSICAL_GOODS_REF
(
    id         VARCHAR(100)                                     NOT NULL PRIMARY KEY,
    order_id   VARCHAR(100) REFERENCES STEP_ORDER (id)          NOT NULL,
    user_id    VARCHAR(100) REFERENCES STEP_USER (id)           NOT NULL,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT (id)        NOT NULL,
    goods_id   VARCHAR(100) REFERENCES STEP_PHYSICAL_GOODS (id) NOT NULL
);

-- 订单与虚拟产品关系
create TABLE STEP_ORDER_VIRTUAL_GOODS_REF
(
    id         VARCHAR(100)                                    NOT NULL PRIMARY KEY,
    order_id   VARCHAR(100) REFERENCES STEP_ORDER (id)         NOT NULL,
    user_id    VARCHAR(100) REFERENCES STEP_USER (id)          NOT NULL,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT (id)       NOT NULL,
    goods_id   VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id) NOT NULL
);

INSERT INTO STEP_VIRTUAL_CATEGORY (id, name)
VALUES ('1', '会员');

INSERT INTO STEP_VIRTUAL_GOODS (id, category_id, name, to_add_month)
VALUES ('1', '1', '月会员', '1');
INSERT INTO STEP_VIRTUAL_GOODS (id, category_id, name, to_add_month)
VALUES ('2', '1', '季度会员', '3');
INSERT INTO STEP_VIRTUAL_GOODS (id, category_id, name, to_add_month)
VALUES ('3', '1', '年会员', '12');
