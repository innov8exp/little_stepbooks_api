-- fix bug

ALTER TABLE "public"."step_paired_read"
    ALTER COLUMN video_url TYPE TEXT,
    ALTER COLUMN cover_img_url TYPE TEXT;
