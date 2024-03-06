-- 广告的 product_id 字段可以为空，实际我们不再使用这个字段，改用更通用的 action_url

ALTER TABLE "public"."step_advertisement"
    ALTER COLUMN product_id DROP NOT NULL;
