-- 用户的章节（有声书）领取历史
create TABLE STEP_BOOK_CHAPTER_RECEIVE_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100),
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL, -- 三个小小人
    chapters      VARCHAR(250), -- '*' 表示领取本书全部章节，'6,7,8,9,10'表示领取ID为6~10的5个章节（有声书）
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 用户的课程领取历史
create TABLE STEP_BOOK_COURSE_RECEIVE_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    user_id       VARCHAR(100),
    book_id       VARCHAR(100) REFERENCES STEP_BOOK(id) NOT NULL, -- 三个小小人
    courses       VARCHAR(250), -- '*' 表示领取本书全部课程，'10,11,12,13,14'表示领取ID为10~14的5个课程
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

