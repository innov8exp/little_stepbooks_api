

-- 资源信息
create TABLE STEP_MEDIA
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    file_name VARCHAR(200) NOT NULL,
    file_size BIGINT,
    public_access BOOLEAN DEFAULT (false),
    object_name VARCHAR(200) NOT NULL UNIQUE,
    object_type VARCHAR(20), -- IMAGE, AUDIO, VIDEO
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
    role          VARCHAR(20) NOT NULL, -- NORMAL_USER, GUEST,
    device_id     VARCHAR(200) NOT NULL,
    nickname      VARCHAR(255),
    phone         VARCHAR(20),
    avatar_img_id VARCHAR(100) REFERENCES STEP_MEDIA(id),
    avatar_img_url    TEXT,
    gender        VARCHAR(20),
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
    sku_no VARCHAR(200) NOT NULL UNIQUE,
    sku_name VARCHAR(200) NOT NULL,
    sales_platform TEXT[], -- ANDROID, IOS, MINI_PROGRAM
    has_inventory BOOLEAN,
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
    inventory_amount INTEGER,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 订单信息
create TABLE STEP_ORDER
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    product_id      VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    order_no        VARCHAR(100) NOT NULL UNIQUE,
    transaction_amount          DECIMAL,
    coin_amount     DECIMAL,
    status          VARCHAR(20),    -- UNPAID, PAID
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 支付信息
create TABLE STEP_PAYMENT
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id        VARCHAR(100) REFERENCES STEP_ORDER(id) NOT NULL,
    order_no        VARCHAR(100) NOT NULL,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    payment_method  VARCHAR(20),    -- WECHAT_PAY, ALI_PAY
    transaction_amount          DECIMAL,
    vendor_payment_no         VARCHAR(200) NOT NULL UNIQUE,
    receipt         VARCHAR(200),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 促销信息
create TABLE STEP_PROMOTION
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id             VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    promotion_type      VARCHAR(20),    -- LIMIT_FREE, LIMIT_DISCOUNT
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
    recommend_type VARCHAR(20), -- FIX, RANDOM, TAG
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

-- 管理员用户信息
create TABLE STEP_ADMIN_USER
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password      VARCHAR(200) NOT NULL,
    role          VARCHAR(20) NOT NULL,
    nickname      VARCHAR(255),
    phone         VARCHAR(20),
    avatar_img    VARCHAR(200),
    active        BOOLEAN DEFAULT (true),
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
    sms_type    VARCHAR(50),  -- VERIFICATION, PROMOTION
    valid_seconds BIGINT,
    status        VARCHAR(50),  -- SUCCESS, FAILED
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- Email发送记录
create TABLE STEP_EMAIL_HISTORY (
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    email         VARCHAR(100) NOT NULL,
    verification_code VARCHAR(50),
    valid_seconds BIGINT,
    email_type    VARCHAR(50),  -- REGISTER, LINK, FORGET
    status        VARCHAR(50),  -- SUCCESS, FAILED
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 登录认证记录
create TABLE STEP_AUTH_HISTORY (
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    username        VARCHAR(100) NOT NULL,
    email           VARCHAR(100),
    phone           VARCHAR(20),
    wechat_id       VARCHAR(100),
    google_id       VARCHAR(100),
    facebook_id     VARCHAR(100),
    auth_type       VARCHAR(50), -- phone, email, wechat, google, facebook
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

create index step_book_name_index on STEP_BOOK (book_name);
create index step_author_index on STEP_BOOK (author);

alter table STEP_BOOKSHELF add constraint uk_bookshelf_book_user unique (book_id, user_id);
