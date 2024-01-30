ALTER TABLE "public"."step_paired_read_collection"
    ADD COLUMN "detail_img_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default",
    ADD COLUMN "detail_img_url" TEXT COLLATE "pg_catalog"."default";
