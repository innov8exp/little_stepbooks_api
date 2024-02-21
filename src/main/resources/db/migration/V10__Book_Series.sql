-- 系列丛书
create TABLE STEP_BOOK_SERIES
(
    id                  VARCHAR(100) NOT NULL PRIMARY KEY,
    series_name         VARCHAR(100) NOT NULL UNIQUE,                               -- 三个小小人
    classification_id   VARCHAR(100) REFERENCES STEP_CLASSIFICATION(id) NOT NULL,   -- 0-3岁
    description         TEXT,                                                       -- 内容介绍
    cover_img_id        VARCHAR(200),                                               -- 顶部封面图
    cover_img_url       TEXT,
    detail_img_id       VARCHAR(200),                                               -- 详情长图
    detail_img_url      TEXT,
    created_at          TIMESTAMP,
    modified_at         TIMESTAMP
);

-- STEP_BOOK 增加 series_id 外键
ALTER TABLE "public"."step_book"
    ADD COLUMN "series_id" VARCHAR(100) REFERENCES STEP_BOOK_SERIES(id);
