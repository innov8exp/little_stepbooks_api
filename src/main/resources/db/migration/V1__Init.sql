

-- 资源信息
create TABLE STEP_MEDIA
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    file_name VARCHAR(200) NOT NULL,
    file_size BIGINT,
    public_access BOOLEAN DEFAULT (false),
    object_name VARCHAR(200) NOT NULL UNIQUE,
    object_type VARCHAR(100), -- IMAGE, AUDIO, VIDEO
    s3_object_id VARCHAR(200) NOT NULL UNIQUE,
    s3_bucket VARCHAR(200),
    store_path VARCHAR(200),
    object_url TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);


-- 书籍分级
create TABLE STEP_CLASSIFICATION
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    classification_name VARCHAR(200) NOT NULL UNIQUE,
    min_age       INTEGER,
    max_age       INTEGER,
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
    avatar_img    VARCHAR(200),
    active        BOOLEAN DEFAULT (true),
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);


-- 用户信息
create TABLE STEP_USER
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(100),
    google_id     VARCHAR(100),
    facebook_id   VARCHAR(100),
    wechat_id     VARCHAR(100),
    alipay_id     VARCHAR(100),
    open_id       VARCHAR(255),
    password      VARCHAR(200),
    role          VARCHAR(100) NOT NULL, -- NORMAL_USER, GUEST,
    device_id     VARCHAR(200) NOT NULL,
    nickname      VARCHAR(255),
    phone         VARCHAR(100),
    avatar_img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    avatar_img_url    TEXT,
    gender        VARCHAR(100),
    child_classification_id VARCHAR(100) REFERENCES STEP_CLASSIFICATION(id),
    child_min_age        INTEGER,
    child_max_age        INTEGER,
    active        BOOLEAN DEFAULT (true),
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
    book_img_url       TEXT,
    description        TEXT,
    total_page_number   INTEGER,
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
    duration BIGINT NOT NULL,
    cover_img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    cover_img_url TEXT,
    video_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    video_url TEXT,
    trial BOOLEAN DEFAULT (false),
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
    sales_platform TEXT[], -- ANDROID, IOS, MINI_PROGRAM
    product_nature VARCHAR(100), -- PHYSICAL, VIRTUAL
    description TEXT,
    price MONEY,
    resources TEXT[], -- AUDIO, COURSE, EXERCISE
    cover_img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    cover_img_url TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 产品SKU与套装关系
create TABLE STEP_PRODUCT_BOOK_SET_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    book_set_id VARCHAR(100) REFERENCES STEP_BOOK_SET(id) NOT NULL
);

-- 产品SKU与课程关系
create TABLE STEP_PRODUCT_COURSE_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    course_id VARCHAR(100) REFERENCES STEP_COURSE(id) NOT NULL
);

-- 产品与媒体关系
create TABLE STEP_PRODUCT_MEDIA_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    media_id VARCHAR(100) REFERENCES STEP_MEDIA(id) NOT NULL
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
    order_type      VARCHAR(100),    -- PURCHASE, REFUND
    total_amount    DECIMAL,
    discount_amount DECIMAL,
    recipient_phone VARCHAR(100),
    payment_timeout_duration BIGINT,
    product_nature      VARCHAR(100), -- PHYSICAL, VIRTUAL
    payment_status  VARCHAR(100),    -- UNPAID, PAID
    state          VARCHAR(100),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 订单产品信息
create TABLE STEP_ORDER_PRODUCT_REF
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id        VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    product_id      VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    quantity        INTEGER,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
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
    order_id      VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    order_code      VARCHAR(100) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    shipper_user_id  VARCHAR(100) REFERENCES STEP_ADMIN_USER(id),
    delivery_method VARCHAR(100), -- EXPRESS, ONLINE
    delivery_status VARCHAR(100), -- WAITING, DELIVERING, DELIVERED, CANCELED
    delivery_code   VARCHAR(200),
    delivery_company VARCHAR(200),
    recipient_name  VARCHAR(100),
    recipient_phone VARCHAR(100),
    recipient_address TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 退款申请
create TABLE STEP_ORDER_REFUND_REQUEST
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    order_code VARCHAR(100) NOT NULL,
    user_id VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    refund_amount DECIMAL,
    refund_reason TEXT,
    refund_status VARCHAR(100), -- PENDING, APPROVED, REJECTED
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 促销信息
create TABLE STEP_PROMOTION
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id             VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    promotion_type      VARCHAR(100),    -- LIMIT_FREE, LIMIT_DISCOUNT
    coin_amount         DECIMAL,
    limit_from           TIMESTAMP,
    limit_to             TIMESTAMP,
    discount_percent    DECIMAL,
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- 用户书架
create TABLE STEP_BOOKSHELF
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    book_id         VARCHAR(100) REFERENCES STEP_BOOK(id),
    course_id       VARCHAR(100) REFERENCES STEP_COURSE(id),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 用户评论信息
create TABLE STEP_COMMENT
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    content       TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户评分信息
create TABLE STEP_RATING
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    rating        SMALLINT,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
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

-- 用户搜索记录
create TABLE STEP_SEARCH_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100),
    keywords      VARCHAR(200) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 推荐书籍信息
create TABLE STEP_RECOMMENDATION
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    sort_index    SERIAL NOT NULL,
    introduction  TEXT,
    recommend_type VARCHAR(100), -- FIX, RANDOM, TAG
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户阅读时常记录
create TABLE STEP_READING_TIME
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    duration      BIGINT,
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

-- Email发送记录
create TABLE STEP_EMAIL_HISTORY (
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    email         VARCHAR(200) NOT NULL,
    verification_code VARCHAR(100),
    valid_seconds BIGINT,
    email_type    VARCHAR(100),  -- REGISTER, LINK, FORGET
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
