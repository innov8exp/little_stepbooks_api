
-- STEP_BOOK 增加 series_no 字段，配合 series_id 一起，表示是这个系列的第几本书
ALTER TABLE "public"."step_book"
    ADD COLUMN "series_no" INTEGER;
