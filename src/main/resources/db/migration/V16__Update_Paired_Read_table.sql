-- 增加视频支持

ALTER TABLE "public"."step_paired_read"
    ADD COLUMN type VARCHAR(100),
    ADD COLUMN video_id VARCHAR(100),
    ADD COLUMN video_url VARCHAR(100),
    ADD COLUMN cover_img_id VARCHAR(100),
    ADD COLUMN cover_img_url VARCHAR(100);
