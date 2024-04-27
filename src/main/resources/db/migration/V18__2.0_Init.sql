-- 详情图
create TABLE STEP_DETAIL_IMAGE
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY, -- 主键ID
    name        TEXT         NOT NULL,             -- 详情图名称
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 详情图的具体切图
create TABLE STEP_DETAIL_IMAGE_CUT
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,              -- 主键ID
    detail_img_id VARCHAR(100) REFERENCES STEP_DETAIL_IMAGE (id), -- 详情图ID
    img_id        VARCHAR(100) REFERENCES STEP_MEDIA (id),        -- 图片ID
    img_url       TEXT,                                           -- 图片链接
    sort_index    SERIAL,                                         -- 排序
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 物理产品
create TABLE STEP_PHYSICAL_GOODS
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,              -- 主键ID
    name          TEXT         NOT NULL,                          -- 产品名称
    description   TEXT,                                           -- 产品描述
    cover_id      VARCHAR(100) REFERENCES STEP_MEDIA (id),        -- 图片ID
    cover_url     TEXT,                                           -- 图片链接
    detail_img_id VARCHAR(100) REFERENCES STEP_DETAIL_IMAGE (id), -- 详情图ID
    status        VARCHAR(100),                                   -- ONLINE/OFFLINE
    sort_index    SERIAL,                                         -- 排序
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 虚拟产品大类
create TABLE STEP_VIRTUAL_CATEGORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,              -- 主键ID
    type          VARCHAR(100),                                   -- MEMBER(会员)/MEDIA(音视频)
    name          TEXT         NOT NULL,                          -- 大类名称
    cover_id      VARCHAR(100) REFERENCES STEP_MEDIA (id),        -- 封面图片ID
    cover_url     TEXT,                                           -- 封面图片链接
    detail_img_id VARCHAR(100) REFERENCES STEP_DETAIL_IMAGE (id), -- 详情图ID
    status        VARCHAR(100),                                   -- ONLINE/OFFLINE
    sort_index    SERIAL,                                         -- 排序
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 虚拟产品（小类）
create TABLE STEP_VIRTUAL_GOODS
(
    id           VARCHAR(100) NOT NULL PRIMARY KEY,                  -- 主键ID
    category_id  VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id), -- 大类ID
    name         TEXT         NOT NULL,                              -- 产品名称
    description  TEXT,                                               -- 产品描述
    to_add_month SMALLINT,                                           -- 增加几个月有效期，对于会员可以是1，3，12个月，对于其他虚拟产品，默认是12个月
    sort_index   SERIAL,                                             -- 排序
    created_at   TIMESTAMP,
    modified_at  TIMESTAMP
);

-- 虚拟产品小类包含的音频
create TABLE STEP_VIRTUAL_GOODS_AUDIO
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,                  -- 主键ID
    category_id VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id), -- 虚拟产品大类ID
    goods_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id),    -- 虚拟产品小类ID
    name        TEXT         NOT NULL,                              -- 音频名称
    cover_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),            -- 封面图片ID
    cover_url   TEXT,                                               -- 封面图片链接
    audio_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),            -- 音频ID
    audio_url   TEXT,                                               -- 音频链接
    duration    VARCHAR(100),                                       -- 时长，格式03:12
    sort_index  SERIAL,                                             -- 排序
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 虚拟产品小类包含的视频
create TABLE STEP_VIRTUAL_GOODS_VIDEO
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,                  -- 主键ID
    category_id VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id), -- 虚拟产品大类ID
    goods_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id),    -- 虚拟产品小类ID
    name        TEXT         NOT NULL,                              -- 视频名称
    cover_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),            -- 封面图片ID
    cover_url   TEXT,                                               -- 封面图片链接
    video_id    VARCHAR(100) REFERENCES STEP_MEDIA (id),            -- 视频ID
    video_url   TEXT,                                               -- 视频链接
    duration    VARCHAR(100),                                       -- 时长，格式03:12
    sort_index  SERIAL,                                             -- 排序
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 虚拟产品小类包含的课程（1.0版本的course）
create TABLE STEP_VIRTUAL_GOODS_COURSE
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,                  -- 主键ID
    category_id VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id), -- 虚拟产品大类ID
    goods_id    VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id),    -- 虚拟产品小类ID
    course_id   VARCHAR(100) REFERENCES STEP_COURSE (id),           -- 课程ID（每个课程下面还有一些具体练习）
    sort_index  SERIAL,                                             -- 排序
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);


-- SKU表
-- 注：STEP_PRODUCT 就是 SPU表，其中的 sku_code, sku_name, price 这3个字段不再使用
create TABLE STEP_SKU
(
    id             VARCHAR(100) NOT NULL PRIMARY KEY,
    spu_id         VARCHAR(200) REFERENCES STEP_PRODUCT (id), -- STEP_PRODUCT 就是 SPU 表，其id为spu_id
    sku_name       VARCHAR(200) NOT NULL,
    original_price DECIMAL,                                   -- 原价
    price          DECIMAL,                                   -- 实际价格
    status         VARCHAR(100) default ('OFF_SHELF'),        -- ON_SHELF, OFF_SHELF
    created_at     TIMESTAMP,
    modified_at    TIMESTAMP
);

-- SKU产品与物理产品关系
create TABLE STEP_SKU_PHYSICAL_GOODS_REF
(
    id       VARCHAR(100)                                     NOT NULL PRIMARY KEY,
    spu_id   VARCHAR(100) REFERENCES STEP_PRODUCT (id)        NOT NULL,
    sku_id   VARCHAR(100) REFERENCES STEP_SKU (id)            NOT NULL,
    goods_id VARCHAR(100) REFERENCES STEP_PHYSICAL_GOODS (id) NOT NULL
);

-- SKU产品与虚拟产品关系（如果goods_id为空，则表示关联整个虚拟产品大类）
create TABLE STEP_SKU_VIRTUAL_GOODS_REF
(
    id               VARCHAR(100)                                       NOT NULL PRIMARY KEY,
    redeem_condition VARCHAR(100), -- PAYMENT_SUCCESS (支付成功立即兑换), SIGN_SUCCESS (签收成功兑换)
    spu_id           VARCHAR(100) REFERENCES STEP_PRODUCT (id)          NOT NULL,
    sku_id           VARCHAR(100) REFERENCES STEP_SKU (id)              NOT NULL,
    category_id      VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id) NOT NULL,
    goods_id         VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id)
);


-- 会员过期状态记录
create TABLE STEP_MEMBER_EXPIRATION
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,      -- 主键ID
    user_id       VARCHAR(100) REFERENCES STEP_USER (id), -- 用户ID
    active_at     TIMESTAMP,                              -- 最近一次激活时间
    expiration_at TIMESTAMP,                              -- 过期时间
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 虚拟产品过期状态记录（如果goods_id为空，则表示拥有整个虚拟产品大类）
create TABLE STEP_VIRTUAL_GOODS_EXPIRATION
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,                  -- 主键ID
    user_id       VARCHAR(100) REFERENCES STEP_USER (id),             -- 用户ID
    category_id   VARCHAR(100) REFERENCES STEP_VIRTUAL_CATEGORY (id), -- 虚拟产品大类ID
    goods_id      VARCHAR(100) REFERENCES STEP_VIRTUAL_GOODS (id),    -- 虚拟产品ID
    active_at     TIMESTAMP,                                          -- 最近一次激活时间
    expiration_at TIMESTAMP,                                          -- 过期时间
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 订单SKU信息
create TABLE STEP_ORDER_SKU_REF
(
    id          VARCHAR(100)                              NOT NULL PRIMARY KEY,
    order_id    VARCHAR(100) REFERENCES STEP_ORDER (id)   NOT NULL,
    spu_id      VARCHAR(100) REFERENCES STEP_PRODUCT (id) NOT NULL,
    sku_id      VARCHAR(100) REFERENCES STEP_SKU (id)     NOT NULL,
    quantity    INTEGER,
    created_at  TIMESTAMP,
    modified_at TIMESTAMP
);

-- 给产品SKU信息增加"标签","视频展示"以及"详情图"字段
ALTER TABLE STEP_PRODUCT
    ADD COLUMN tags VARCHAR(100), -- 图书\文创\课程\训练营，支持用逗号分隔的多个标签
    ADD COLUMN video_id VARCHAR(100) REFERENCES STEP_MEDIA (id),
    ADD COLUMN video_url TEXT,
    ADD COLUMN detail_img_id VARCHAR(100) REFERENCES STEP_DETAIL_IMAGE (id);


INSERT INTO STEP_VIRTUAL_CATEGORY (id, name, type)
VALUES ('1', '会员', 'MEMBER');

INSERT INTO STEP_VIRTUAL_GOODS (id, category_id, name, to_add_month)
VALUES ('1', '1', '月会员', '1');
INSERT INTO STEP_VIRTUAL_GOODS (id, category_id, name, to_add_month)
VALUES ('2', '1', '季度会员', '3');
INSERT INTO STEP_VIRTUAL_GOODS (id, category_id, name, to_add_month)
VALUES ('3', '1', '年会员', '12');
