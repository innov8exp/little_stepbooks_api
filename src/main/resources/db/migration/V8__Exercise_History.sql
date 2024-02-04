-- 用户练习历史记录（插入规制是如果分数比上次高，则使用高分替换低分，总分定义见 AbstractExercise.point ）
create TABLE STEP_EXERCISE_HISTORY
(
    id            VARCHAR(100) NOT NULL PRIMARY KEY,
    exercise_id   VARCHAR(100) REFERENCES STEP_EXERCISE(id) NOT NULL,
    course_id     VARCHAR(100) REFERENCES STEP_COURSE(id) NOT NULL,
    user_id       VARCHAR(100),
    score         INTEGER,
    created_at    TIMESTAMP,
    modified_at   TIMESTAMP
);

-- 伴读营增加上线下线机制 ONLINE/OFFLINE
ALTER TABLE "public"."step_paired_read_collection"
    ADD COLUMN "status" VARCHAR(100);

-- 广告位增加一个actionUrl字段，可以是http/https开头的网页，或者是stepbook://开头的本地协议跳转
ALTER TABLE "public"."step_advertisement"
    ADD COLUMN "action_url" TEXT;
