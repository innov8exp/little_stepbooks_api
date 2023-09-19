
-- 用户信息
CREATE TABLE NOVL_USER
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(100),
    google_id     VARCHAR(100),
    facebook_id   VARCHAR(100),
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
CREATE TABLE NOVL_USER_ACCOUNT
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    coin_balance    DECIMAL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 小说信息
CREATE TABLE NOVL_BOOK
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    book_name           VARCHAR(200) NOT NULL UNIQUE,
    author              VARCHAR(200),
    cover_img           VARCHAR(200),
    introduction        TEXT,
    keywords            VARCHAR[],
    is_serialized       BOOLEAN DEFAULT(false),
    has_ending          BOOLEAN DEFAULT(true),
    content_location    VARCHAR(200),
    status              VARCHAR(20),    -- OFFLINE, ONLINE
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- 章节信息
CREATE TABLE NOVL_CHAPTER
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    chapter_number  INTEGER NOT NULL,
    chapter_name    VARCHAR(200) NOT NULL,
    introduction    TEXT,
    book_id         VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    content_link    TEXT,
    total_paragraph_number BIGINT,
    need_pay        BOOLEAN default(false),
    store_key       VARCHAR(200),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 定价详情
CREATE TABLE NOVL_PRICE
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id             VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    charge_type         VARCHAR(20), -- FREE, FULL_CHARGE, PART_CHARGE
    price               DECIMAL,
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- 促销信息
CREATE TABLE NOVL_PROMOTION
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id             VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    promotion_type      VARCHAR(20),    -- LIMIT_FREE, LIMIT_DISCOUNT
    coin_amount         DECIMAL,
    limit_from           TIMESTAMP,
    limit_to             TIMESTAMP,
    discount_percent    DECIMAL,
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- 充值产品
CREATE TABLE NOVL_PRODUCT
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    product_no          VARCHAR(200) NOT NULL UNIQUE,
    coin_amount         DECIMAL,
    price               DECIMAL,
    platform            VARCHAR(20), --ANDROID, IOS
    store_product_id    VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- 订单信息
CREATE TABLE NOVL_ORDER
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    product_id      VARCHAR(100) REFERENCES NOVL_PRODUCT(id) NOT NULL,
    order_no        VARCHAR(100) NOT NULL UNIQUE,
    transaction_amount          DECIMAL,
    coin_amount     DECIMAL,
    status          VARCHAR(20),    -- UNPAID, PAID
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 支付信息
CREATE TABLE NOVL_PAYMENT
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    order_id        VARCHAR(100) REFERENCES NOVL_ORDER(id) NOT NULL,
    order_no        VARCHAR(100) NOT NULL,
    user_id         VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    payment_method  VARCHAR(20),    -- APPLE_PAY, GOOGLE_PAY
    transaction_amount          DECIMAL,
    vendor_payment_no         VARCHAR(200) NOT NULL UNIQUE,
    receipt         VARCHAR(200),
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 消费明细
CREATE TABLE NOVL_CONSUMPTION
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    book_id         VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    chapter_id      VARCHAR(100) REFERENCES NOVL_CHAPTER(id) NOT NULL,
    consume_order_no        VARCHAR(100) NOT NULL UNIQUE,
    client_platform   VARCHAR(20),    -- IOS, ANDROID
    coin_amount     DECIMAL,
    consume_type    VARCHAR(20),    -- SYSTEM_GIFT, RECHARGE
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 用户书架
CREATE TABLE NOVL_BOOKSHELF
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id         VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    sort_index      SERIAL NOT NULL,
    user_id         VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 用户收藏
CREATE TABLE NOVL_FAVORITE
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id         VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    sort_index      SERIAL NOT NULL,
    user_id         VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 书籍分类
CREATE TABLE NOVL_CATEGORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    category_name VARCHAR(200) NOT NULL UNIQUE,
    description   TEXT,
    sort_index    SERIAL NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 书籍分类与书籍关系
CREATE TABLE NOVL_BOOK_CATEGORY_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    category_id   VARCHAR(100) REFERENCES NOVL_CATEGORY(id) NOT NULL
);

-- 标签信息
CREATE TABLE NOVL_TAG
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    tag_name      VARCHAR(200) NOT NULL UNIQUE,
    description   TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 书籍标签关系
CREATE TABLE NOVL_BOOK_TAG_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    tag_id        VARCHAR(100) REFERENCES NOVL_TAG(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户标签关系
CREATE TABLE NVOL_USER_TAG_REF
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id        VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    tag_id        VARCHAR(100) REFERENCES NOVL_TAG(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户书籍书签
CREATE TABLE NOVL_BOOKMARK
(
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id         VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    chapter_id      VARCHAR(100) REFERENCES NOVL_CHAPTER(id) NOT NULL,
    paragraph_number      BIGINT,
    first_line_content  VARCHAR(200),
    user_id         VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);

-- 用户评论信息
CREATE TABLE NOVL_COMMENT
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    content       TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户评分信息
CREATE TABLE NOVL_RATING
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    rating        SMALLINT,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户阅读历史信息
CREATE TABLE NOVL_READING_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    chapter_id    VARCHAR(100) REFERENCES NOVL_CHAPTER(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    paragraph_number BIGINT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户浏览记录
CREATE TABLE NOVL_VIEW_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户喜欢记录
CREATE TABLE NOVL_LIKE_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户搜索记录
CREATE TABLE NOVL_SEARCH_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    user_id       VARCHAR(100),
    keywords      VARCHAR(200) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 广告投放信息
CREATE TABLE NOVL_ADVERTISEMENT
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    sort_index    SERIAL,
    ads_img       VARCHAR(200),
    introduction  TEXT,
    ads_type      VARCHAR(20), -- RECOMMEND, CAROUSEL
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 推荐书籍信息
CREATE TABLE NOVL_RECOMMEND
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    sort_index    SERIAL NOT NULL,
    introduction  TEXT,
    recommend_type VARCHAR(20), -- FIX, RANDOM, TAG
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户阅读时常记录
CREATE TABLE NOVL_READING_TIME
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    duration      BIGINT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 管理员用户信息
CREATE TABLE NOVL_ADMIN_USER
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
CREATE TABLE NOVL_FEEDBACK
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    content       TEXT,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

CREATE TABLE NOVL_FINISH_HISTORY (
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    book_id       VARCHAR(100) REFERENCES NOVL_BOOK(id) NOT NULL,
    user_id       VARCHAR(100) REFERENCES NOVL_USER(id) NOT NULL,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

CREATE TABLE NOVL_EMAIL_HISTORY (
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    email         VARCHAR(100) NOT NULL,
    verification_code VARCHAR(50),
    valid_seconds BIGINT,
    email_type    VARCHAR(50),  -- REGISTER, LINK, FORGET
    status        VARCHAR(50),  -- SUCCESS, FAILED
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

CREATE TABLE NOVL_AUTH_HISTORY (
    id              VARCHAR(100) NOT NULL PRIMARY KEY,
    username        VARCHAR(100) NOT NULL,
    email           VARCHAR(100),
    google_id       VARCHAR(100),
    facebook_id     VARCHAR(100),
    auth_type       VARCHAR(50), -- email, google, facebook
    created_at      TIMESTAMP,
    modified_at     TIMESTAMP
);


CREATE INDEX novl_book_name_index ON NOVL_BOOK (book_name);
CREATE INDEX novl_keywords_index ON NOVL_BOOK (keywords);
CREATE INDEX novl_author_index ON NOVL_BOOK (author);

alter table NOVL_BOOKSHELF add constraint uk_bookshelf_book_user unique (book_id, user_id);
alter table NOVL_FAVORITE add constraint uk_favorite_book_user unique (book_id, user_id);

--CREATE TABLE NOVL_AUTHOR
--(
--    id            VARCHAR(100) NOT NULL PRIMARY KEY,
--    email         VARCHAR(100),
--    name          VARCHAR(255),
--    introduction  TEXT,
--    created_at    TIMESTAMP,
--    modified_at   TIMESTAMP
--);


