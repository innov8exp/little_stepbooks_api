-- 增加排序

ALTER TABLE "public"."step_paired_read"
    ADD COLUMN sort_index SERIAL;
