ALTER TABLE "public"."step_paired_read_collection_user" RENAME COLUMN "user_id" TO "username";
ALTER TABLE "public"."step_paired_read_collection_user"
  ALTER COLUMN "username" TYPE varchar(255) COLLATE "pg_catalog"."default";

	ALTER TABLE "public"."step_paired_read_collection_user"
  DROP CONSTRAINT "step_paired_read_collection_user_user_id_fkey",
  ADD CONSTRAINT "step_paired_read_collection_user_user_id_fkey" FOREIGN KEY ("username") REFERENCES "public"."step_user" ("username") ON DELETE NO ACTION ON UPDATE NO ACTION;