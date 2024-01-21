CREATE TABLE "public"."step_paired_read_collection" (
	"id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"cover_img_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default",
	"cover_img_url" TEXT COLLATE "pg_catalog"."default",
	"name" VARCHAR ( 200 ) COLLATE "pg_catalog"."default" NOT NULL,
	"description" VARCHAR ( 200 ) COLLATE "pg_catalog"."default" NOT NULL,
	"created_at" TIMESTAMP ( 6 ),
	"modified_at" TIMESTAMP ( 6 ),
	CONSTRAINT "step_paired_read_collection_pkey" PRIMARY KEY ( "id" ),
	CONSTRAINT "step_paired_read_collection_cover_img_id_fkey" FOREIGN KEY ( "cover_img_id" ) REFERENCES "public"."step_media" ( "id" ) ON DELETE NO ACTION ON UPDATE NO ACTION 
);

CREATE TABLE "public"."step_paired_read" (
	"id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"collection_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"name" VARCHAR ( 200 ) COLLATE "pg_catalog"."default",
	"duration" VARCHAR ( 100 ) COLLATE "pg_catalog"."default",
	"audio_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default",
	"audio_url" TEXT COLLATE "pg_catalog"."default",
	"created_at" TIMESTAMP ( 6 ),
	"modified_at" TIMESTAMP ( 6 ),
	CONSTRAINT "step_paired_read_pkey" PRIMARY KEY ( "id" ),
	CONSTRAINT "step_paired_read_collection_user_collection_id_fkey" FOREIGN KEY ( "collection_id" ) REFERENCES "public"."step_paired_read_collection" ( "id" ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT "step_paired_read_audio_id_fkey" FOREIGN KEY ( "audio_id" ) REFERENCES "public"."step_media" ( "id" ) ON DELETE NO ACTION ON UPDATE NO ACTION 
);

CREATE TABLE "public"."step_paired_read_collection_user" (
	"id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"collection_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"user_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"created_at" TIMESTAMP ( 6 ),
	"modified_at" TIMESTAMP ( 6 ),
	CONSTRAINT "step_paired_read_collection_user_pkey" PRIMARY KEY ( "id" ),
	CONSTRAINT "step_paired_read_collection_user_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."step_user" ( "id" ) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "step_paired_read_collection_user_collection_id_fkey" FOREIGN KEY ( "collection_id" ) REFERENCES "public"."step_paired_read_collection" ( "id" ) ON DELETE NO ACTION ON UPDATE NO ACTION 
);


CREATE TABLE "public"."step_exercise" (
	"id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"course_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default" NOT NULL,
	"name" VARCHAR ( 200 ) COLLATE "pg_catalog"."default",
	"nature" VARCHAR ( 200 ) COLLATE "pg_catalog"."default",
	"sort_no" int4 NOT NULL,
	"cover_img_id" VARCHAR ( 100 ) COLLATE "pg_catalog"."default",
	"cover_img_url" TEXT COLLATE "pg_catalog"."default",
	"json" TEXT COLLATE "pg_catalog"."default",
	"answer_json" TEXT COLLATE "pg_catalog"."default",
	"created_at" TIMESTAMP ( 6 ),
	"modified_at" TIMESTAMP ( 6 ),
	CONSTRAINT "step_exercise_pkey" PRIMARY KEY ( "id" ),
	CONSTRAINT "step_exercise_cover_img_id_fkey" FOREIGN KEY ( "cover_img_id" ) REFERENCES "public"."step_media" ( "id" ) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "step_exercise_course_id_fkey" FOREIGN KEY ( "course_id" ) REFERENCES "public"."step_course" ( "id" ) ON DELETE NO ACTION ON UPDATE NO ACTION
);