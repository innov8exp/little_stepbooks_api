
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
    password      VARCHAR(200),
    role          VARCHAR(20) NOT NULL, -- NORMAL_USER, GUEST,
    device_id     VARCHAR(200) NOT NULL,
    nickname      VARCHAR(255),
    phone         VARCHAR(20),
    avatar_img    TEXT,
    gender        VARCHAR(20),
    active        BOOLEAN DEFAULT (true),
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户账户信息
create TABLE STEP_USER_ACCOUNT
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    coin_balance    DECIMAL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);


-- 资源信息
create TABLE STEP_MEDIA
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    media_name VARCHAR(200) NOT NULL,
    media_type VARCHAR(20), -- IMAGE, AUDIO, VIDEO
    media_link TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);


-- 书籍信息
create TABLE STEP_BOOK
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    book_name           VARCHAR(200) NOT NULL UNIQUE,
    author              VARCHAR(200),
    book_img_link       TEXT,
    introduction        TEXT,
    keywords            VARCHAR[],
    total_page_number   INTEGER,
    status              VARCHAR(20),    -- DISABLE, ENABLE
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- 书籍内容
create TABLE STEP_BOOK_CONTENT
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    page_discription TEXT,
    page_number INTEGER NOT NULL,
    page_img_link TEXT,
    page_audio_link TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 课程信息
create TABLE STEP_COURSE
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    course_name VARCHAR(200) NOT NULL,
    course_img_link VARCHAR(200),
    course_description TEXT,
    course_author VARCHAR(200),
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 课程内容
create TABLE STEP_COURSE_CHAPTER
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    course_id VARCHAR(100) REFERENCES STEP_COURSE(id) NOT NULL,
    chapter_number INTEGER NOT NULL,
    chapter_name VARCHAR(200) NOT NULL,
    chapter_content_link TEXT,
    chapter_duration VARCHAR(200),
    chapter_content_description TEXT,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 主产品信息
create TABLE STEP_PRODUCT
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_name VARCHAR(200) NOT NULL,
    charge_type VARCHAR(20), -- FREE, FULL_CHARGE, PART_CHARGE
    description TEXT,
    cover_img_link TEXT,
    price DECIMAL,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 产品与资源关系
create TABLE STEP_PRODUCT_MEDIA
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    media_id VARCHAR(100) REFERENCES STEP_MEDIA(id) NOT NULL,
    media_link TEXT,
);

-- 产品SKU信息
create TABLE STEP_SKU
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    sku_no VARCHAR(200) NOT NULL UNIQUE,
    sku_name VARCHAR(200) NOT NULL,
    sales_platform VARCHAR(20), -- ANDROID, IOS, MINI_PROGRAM
    has_inventory BOOLEAN,
    description TEXT,
    price DECIMAL,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 产品SKU与书籍关系
create TABLE STEP_SKU_BOOK_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    sku_id VARCHAR(100) REFERENCES STEP_SKU(id) NOT NULL,
    book_id VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL
);

-- 产品SKU与课程关系
create TABLE STEP_SKU_COURSE_REF
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    sku_id VARCHAR(100) REFERENCES STEP_SKU(id) NOT NULL,
    course_id VARCHAR(100) REFERENCES STEP_COURSE(id) NOT NULL
);

-- SKU与资源关系
create TABLE STEP_SKU_MEDIA
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    sku_id VARCHAR(100) REFERENCES STEP_SKU(id) NOT NULL,
    media_id VARCHAR(100) REFERENCES STEP_MEDIA(id) NOT NULL,
    media_link TEXT,
);

-- 库存信息
create TABLE STEP_INVENTORY
(
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    sku_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
    inventory_amount INTEGER,
    created_at TIMESTAMP,
    modified_at TIMESTAMP
);

-- 购物车
create TABLE STEP_SHOPPING_CART
(
   id VARCHAR(100) NOT NULL PRIMARY KEY,
   user_id VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
   product_id VARCHAR(100) REFERENCES STEP_PRODUCT(id) NOT NULL,
   product_amount DECIMAL,
   created_at          TIMESTAMP,
   modified_at         TIMESTAMP
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
    payment_method  VARCHAR(20),    -- APPLE_PAY, GOOGLE_PAY
    transaction_amount          DECIMAL,
    vendor_payment_no         VARCHAR(200) NOT NULL UNIQUE,
    receipt         VARCHAR(200),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 消费明细
create TABLE STEP_CONSUMPTION
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    book_id         VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    chapter_id      VARCHAR(100) REFERENCES STEP_CHAPTER(id) NOT NULL,
    consume_order_no        VARCHAR(100) NOT NULL UNIQUE,
    client_platform   VARCHAR(20),    -- IOS, ANDROID
    coin_amount     DECIMAL,
    consume_type    VARCHAR(20),    -- SYSTEM_GIFT, RECHARGE
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
    book_id         VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    sort_index      SERIAL NOT NULL,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 用户收藏
create TABLE STEP_FAVORITE
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id         VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    sort_index      SERIAL NOT NULL,
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 书籍分类
create TABLE STEP_CATEGORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    category_name VARCHAR(200) NOT NULL UNIQUE,
    description   TEXT,
    sort_index    SERIAL NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 书籍分类与书籍关系
create TABLE STEP_BOOK_CATEGORY_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    category_id   VARCHAR(100) REFERENCES STEP_CATEGORY(id) NOT NULL
);

-- 标签信息
create TABLE STEP_TAG
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    tag_name      VARCHAR(200) NOT NULL UNIQUE,
    description   TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 书籍标签关系
create TABLE STEP_BOOK_TAG_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    tag_id        VARCHAR(100) REFERENCES STEP_TAG(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户标签关系
create TABLE STEP_USER_TAG_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id        VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    tag_id        VARCHAR(100) REFERENCES STEP_TAG(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户书籍书签
create TABLE STEP_BOOKMARK
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id         VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    chapter_id      VARCHAR(100) REFERENCES STEP_CHAPTER(id) NOT NULL,
    paragraph_number      BIGINT,
    first_line_content  VARCHAR(200),
    user_id         VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
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
    chapter_id    VARCHAR(100) REFERENCES STEP_CHAPTER(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    paragraph_number BIGINT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户浏览记录
create TABLE STEP_VIEW_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户喜欢记录
create TABLE STEP_LIKE_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
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

-- 广告投放信息
create TABLE STEP_ADVERTISEMENT
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    sort_index    SERIAL,
    ads_img       VARCHAR(200),
    introduction  TEXT,
    ads_type      VARCHAR(20), -- RECOMMEND, CAROUSEL
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 推荐书籍信息
create TABLE STEP_RECOMMEND
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

create TABLE STEP_FINISH_HISTORY (
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES STEP_USER(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

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

create TABLE STEP_AUTH_HISTORY (
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    username        VARCHAR(100) NOT NULL,
    email           VARCHAR(100),
    google_id       VARCHAR(100),
    facebook_id     VARCHAR(100),
    auth_type       VARCHAR(50), -- email, google, facebook
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

create index step_book_name_index on STEP_BOOK (book_name);
create index step_keywords_index on STEP_BOOK (keywords);
create index step_author_index on STEP_BOOK (author);

alter table STEP_BOOKSHELF add constraint uk_bookshelf_book_user unique (book_id, user_id);
alter table STEP_FAVORITE add constraint uk_favorite_book_user unique (book_id, user_id);
