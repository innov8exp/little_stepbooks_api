

-- 资源信息
create TABLE STEP_MEDIA
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    file_name TEXT NOT NULL,
    file_size BIGINT,
    access_permission VARCHAR(100),
    asset_domain VARCHAR(100),
    object_name VARCHAR(200) NOT NULL UNIQUE,
    object_type VARCHAR(100), -- IMAGE, AUDIO, VIDEO
    object_key VARCHAR(200) NOT NULL UNIQUE,
    bucket_name VARCHAR(200),
    store_path VARCHAR(200),
    object_url TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 分级
create TABLE STEP_CLASSIFICATION
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    classification_name VARCHAR(200) NOT NULL UNIQUE,
    min_age       REAL,
    max_age       REAL,
    description   TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 管理员用户信息
create TABLE STEP_ADMIN_USER
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password      VARCHAR(200) NOT NULL,
    role          VARCHAR(100) NOT NULL,
    nickname      VARCHAR(255),
    phone         VARCHAR(100),
    avatar_img_id    VARCHAR(100) REFERENCES STEP_MEDIA(id),
    avatar_img_url  TEXT,
    active        BOOLEAN DEFAULT (true),
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户信息
create TABLE STEP_USER
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    role          VARCHAR(100) NOT NULL, -- NORMAL_USER, GUEST,
    phone         VARCHAR(100) NOT NULL UNIQUE,
    email         VARCHAR(100),
    google_id     VARCHAR(100),
    facebook_id   VARCHAR(100),
    wechat_id     VARCHAR(100),
    alipay_id     VARCHAR(100),
    open_id       VARCHAR(255),
    union_id       VARCHAR(255),
    password      VARCHAR(200),
    device_id     VARCHAR(200),
    nickname      VARCHAR(255),
    avatar_img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    avatar_img_url  TEXT,
    child_gender        VARCHAR(100),
    child_classification_id VARCHAR(100) REFERENCES STEP_CLASSIFICATION(id),
    child_min_age        REAL,
    child_max_age        REAL,
    active        BOOLEAN DEFAULT (true),
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户地址信息
create TABLE STEP_USER_ADDRESS
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    recipient_name VARCHAR(100),
    recipient_phone VARCHAR(100),
    recipient_province VARCHAR(100),
    recipient_city VARCHAR(100),
    recipient_district VARCHAR(100),
    recipient_address TEXT,
    is_default BOOLEAN default (false),
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 书籍信息
create TABLE STEP_BOOK
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    book_name           VARCHAR(200) NOT NULL UNIQUE,
    author              VARCHAR(200),
    book_img_id         VARCHAR(100) REFERENCES STEP_MEDIA(id),
    book_img_url    TEXT,
    description        TEXT,
    total_page_number   INTEGER,
    duration            VARCHAR(100),
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- 书籍分级与书籍关系
create TABLE STEP_BOOK_CLASSIFICATION_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    classification_id VARCHAR(100) REFERENCES STEP_CLASSIFICATION(id) NOT NULL
);

-- 书籍章节内容
create TABLE STEP_BOOK_CHAPTER
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    description TEXT,
    chapter_no INTEGER NOT NULL,
    chapter_name VARCHAR(200),
    img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    img_url TEXT,
    audio_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    audio_url TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 课程信息
create TABLE STEP_COURSE
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    author VARCHAR(200),
    author_introduction VARCHAR(200),
    duration VARCHAR(100),
    cover_img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    cover_img_url TEXT,
    video_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    video_url TEXT,
    course_nature VARCHAR(100) DEFAULT ('NEED_TO_PAY'),
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 书籍套装
create TABLE STEP_BOOK_SET
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    code VARCHAR(200) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 书籍套装与书籍关系
create TABLE STEP_BOOK_SET_BOOK_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    book_set_id VARCHAR(100) REFERENCES STEP_BOOK_SET(id) NOT NULL,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL
);

-- 产品SKU信息
create TABLE STEP_PRODUCT
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    sku_code VARCHAR(200) NOT NULL UNIQUE,
    sku_name VARCHAR(200) NOT NULL,
    sales_platforms INT, -- 0: NONE, 1: MINI_PROGRAM, 2: APP, 3: MINI_PROGRAM + APP
    product_nature VARCHAR(100), -- PHYSICAL, VIRTUAL
    description TEXT,
    price DECIMAL,
    materials INT, -- 0: NONE, 1: AUDIO, 2: COURSE, 4: EXERCISE, 3: AUDIO + COURSE, 5: AUDIO + EXERCISE, 6: COURSE + EXERCISE, 7: AUDIO + COURSE + EXERCISE
    cover_img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    cover_img_url TEXT,
    book_set_id VARCHAR(100) REFERENCES STEP_BOOK_SET(id),
    book_set_code VARCHAR(100),
    status VARCHAR(100) default ('OFF_SHELF'), -- ON_SHELF, OFF_SHELF
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 产品与书籍关系
create TABLE STEP_PRODUCT_BOOK_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL
);

-- 产品与课程关系
create TABLE STEP_PRODUCT_COURSE_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    course_id VARCHAR(100) REFERENCES STEP_COURSE(id) NOT NULL
);

-- 产品与分级关系
create TABLE STEP_PRODUCT_CLASSIFICATION_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id       VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    classification_id VARCHAR(100) REFERENCES STEP_CLASSIFICATION(id) NOT NULL
);

-- 产品与媒体关系
create TABLE STEP_PRODUCT_MEDIA_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    media_id VARCHAR(100) REFERENCES STEP_MEDIA(id) NOT NULL,
    media_url TEXT
);

-- 库存信息
create TABLE STEP_INVENTORY
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    inventory_quantity INTEGER,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 订单信息
create TABLE STEP_ORDER
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_code        VARCHAR(100) NOT NULL UNIQUE,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    total_amount    DECIMAL,
    discount_amount DECIMAL,
    payment_amount  DECIMAL,
    refund_amount   DECIMAL,
    recipient_phone VARCHAR(100),
    payment_timeout_duration BIGINT,
    product_nature      VARCHAR(100), -- PHYSICAL, VIRTUAL
    payment_method  VARCHAR(100),    -- WECHAT_PAY, ALI_PAY
    payment_status  VARCHAR(100),    -- UNPAID, PAID
    refund_type    VARCHAR(100),    -- ONLY_REFUND, REFUND_AND_RETURN
    state          VARCHAR(100),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 订单产品信息
create TABLE STEP_ORDER_PRODUCT_REF
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id        VARCHAR(100) REFERENCES STEP_ORDER(id) UNIQUE NOT NULL,
    product_id      VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    quantity        INTEGER,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 订单与书籍信息
create TABLE STEP_ORDER_BOOK_REF
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id        VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    product_id     VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    book_id         VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL
);

-- 订单与课程信息
create TABLE STEP_ORDER_COURSE_REF
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id        VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    product_id     VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    course_id       VARCHAR(100) REFERENCES STEP_COURSE(id) NOT NULL
);

-- 订单事件
create TABLE STEP_ORDER_EVENT_LOG
(
    id             VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id       VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    order_code       VARCHAR(100) NOT NULL,
    from_state    VARCHAR(100),
    to_state    VARCHAR(100),
    event_type     VARCHAR(100),    -- CREATE, PAY, REFUND, CANCEL
    event_time     TIMESTAMP,
    created_at     TIMESTAMP,
    modified_at    TIMESTAMP
);

-- 订单库存流水记录
create TABLE STEP_ORDER_INVENTORY_LOG
(
    id             VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id       VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    order_code       VARCHAR(100) NOT NULL,
    product_id     VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    sku_code       VARCHAR(100) NOT NULL,
    inventory_id   VARCHAR(100) REFERENCES STEP_INVENTORY(id) NOT NULL,
    quantity       INTEGER,
    created_at     TIMESTAMP,
    modified_at    TIMESTAMP
);

-- 支付信息
create TABLE STEP_PAYMENT
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id        VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    order_code        VARCHAR(100) NOT NULL,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    payment_method  VARCHAR(100),    -- WECHAT_PAY, ALI_PAY
    payment_type    VARCHAR(100),    -- ORDER_PAYMENT, REFUND_PAYMENT
    transaction_amount          DECIMAL,
    vendor_payment_no         VARCHAR(200) NOT NULL UNIQUE,
    receipt         VARCHAR(200),
    transaction_status          VARCHAR(100),    -- SUCCESS, FAILED
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 配送信息
create TABLE STEP_DELIVERY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id      VARCHAR(100) REFERENCES STEP_ORDER(id) UNIQUE NOT NULL,
    order_code      VARCHAR(100) UNIQUE NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    shipper_user_id  VARCHAR(100) REFERENCES STEP_ADMIN_USER(id),
    delivery_method VARCHAR(100), -- EXPRESS, ONLINE
    delivery_status VARCHAR(100), -- WAITING, DELIVERING, DELIVERED, CANCELED
    delivery_code   VARCHAR(200),
    delivery_company VARCHAR(200),
    recipient_name  VARCHAR(100),
    recipient_phone VARCHAR(100),
    recipient_province VARCHAR(100),
    recipient_city VARCHAR(100),
    recipient_district VARCHAR(100),
    recipient_address TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 退款申请
create TABLE STEP_REFUND_REQUEST
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    order_code VARCHAR(100) NOT NULL,
    user_id VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    refund_amount DECIMAL,
    refund_reason VARCHAR(200), -- WRONG_PURCHASE, WRONG_PAYMENT, OTHER
    refund_reason_description TEXT,
    request_status VARCHAR(100), -- PENDING, APPROVED, REJECTED
    refund_status VARCHAR(100), -- PENDING, REFUNDING_WAIT_DELIVERY, REFUNDING_WAIT_SIGN, REFUNDED, REFUND_FAILED, REFUND_CANCELLED
    refund_type VARCHAR(100), -- ONLY_REFUND, REFUND_AND_RETURN
    reject_reason TEXT,
    delivery_code   VARCHAR(200),
    delivery_company VARCHAR(200),
    delivery_status VARCHAR(100),
    recipient_name  VARCHAR(100),
    recipient_phone VARCHAR(100),
    recipient_province VARCHAR(100),
    recipient_city VARCHAR(100),
    recipient_district VARCHAR(100),
    recipient_address TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 用户书架
create TABLE STEP_BOOKSHELF
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    book_id         VARCHAR(100) REFERENCES STEP_BOOK(id),
    book_set_id     VARCHAR(100) REFERENCES STEP_BOOK_SET(id),
    book_set_code   VARCHAR(100),
    sort_index      SERIAL,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 套装激活记录
create TABLE STEP_BOOKSHELF_ADD_LOG
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    book_set_id     VARCHAR(100) REFERENCES STEP_BOOK_SET(id),
    book_set_code   VARCHAR(100),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 广告投放信息
CREATE TABLE STEP_ADVERTISEMENT
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id       VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    sort_index    SERIAL,
    ads_img_id       VARCHAR(200) REFERENCES STEP_MEDIA(id),
    ads_img_url   TEXT,
    introduction  TEXT,
    ads_type      VARCHAR(20), -- RECOMMEND, CAROUSEL
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户阅读历史信息
create TABLE STEP_READING_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    paragraph_number BIGINT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户学习时长
create TABLE STEP_LEARN_TIME
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    duration      BIGINT,
    learn_date_time    TIMESTAMP,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户反馈
create TABLE STEP_FEEDBACK
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    content       TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 完成历史记录
create TABLE STEP_FINISH_HISTORY (
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- SMS发送记录
create TABLE STEP_SMS_HISTORY (
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    phone         VARCHAR(100) NOT NULL,
    content       TEXT,
    msg_id        BIGINT,
    sms_type    VARCHAR(100),  -- VERIFICATION, PROMOTION
    valid_seconds BIGINT,
    status        VARCHAR(100),  -- SUCCESS, FAILED
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 登录认证记录
create TABLE STEP_AUTH_HISTORY (
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    username        VARCHAR(200) NOT NULL,
    email           VARCHAR(200),
    phone           VARCHAR(100),
    wechat_id       VARCHAR(200),
    google_id       VARCHAR(100),
    facebook_id     VARCHAR(100),
    auth_type       VARCHAR(100), -- phone, email, wechat, google, facebook
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

create index step_book_name_index on STEP_BOOK (book_name);
create index step_author_index on STEP_BOOK (author);

alter table STEP_BOOKSHELF add constraint uk_bookshelf_book_user unique (book_id, user_id);

--
---- 促销信息
--create TABLE STEP_PROMOTION
--(
--    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
--    book_id             VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
--    promotion_type      VARCHAR(100),    -- LIMIT_FREE, LIMIT_DISCOUNT
--    coin_amount         DECIMAL,
--    limit_from           TIMESTAMP,
--    limit_to             TIMESTAMP,
--    discount_percent    DECIMAL,
--    created_at          TIMESTAMP,
--    modified_at         TIMESTAMP
--);



-- 用户评论信息
--create TABLE STEP_COMMENT
--(
--    id            VARCHAR(100) NOT NULL PRIMARY KEY,
--    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
--    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
--    content       TEXT,
--    created_at    TIMESTAMP,
--    modified_at   TIMESTAMP
--);

-- 用户评分信息
--create TABLE STEP_RATING
--(
--    id            VARCHAR(100) NOT NULL PRIMARY KEY,
--    rating        SMALLINT,
--    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
--    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
--    created_at    TIMESTAMP,
--    modified_at   TIMESTAMP
--);

---- 推荐书籍信息
--create TABLE STEP_RECOMMENDATION
--(
--    id            VARCHAR(100) NOT NULL PRIMARY KEY,
--    product_id    VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
--    sort_index    SERIAL NOT NULL,
--    introduction  TEXT,
--    recommend_type VARCHAR(100), -- FIX, RANDOM, TAG
--    created_at    TIMESTAMP,
--    modified_at   TIMESTAMP
--);


-- 用户搜索记录
--create TABLE STEP_SEARCH_HISTORY
--(
--    id            VARCHAR(100) NOT NULL PRIMARY KEY,
--    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
--    user_id       VARCHAR(100),
--    keywords      VARCHAR(200) NOT NULL,
--    created_at    TIMESTAMP,
--    modified_at   TIMESTAMP
--);

-- Email发送记录
--create TABLE STEP_EMAIL_HISTORY (
--    id            VARCHAR(100) NOT NULL PRIMARY KEY,
--    email         VARCHAR(200) NOT NULL,
--    verification_code VARCHAR(100),
--    valid_seconds BIGINT,
--    email_type    VARCHAR(100),  -- REGISTER, LINK, FORGET
--    status        VARCHAR(100),  -- SUCCESS, FAILED
--    created_at    TIMESTAMP,
--    modified_at   TIMESTAMP
--);
