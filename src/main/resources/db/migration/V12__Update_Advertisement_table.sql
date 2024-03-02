-- 广告位增加一个wxActionUrl字段，表示微信小程序的页面访问路径
ALTER TABLE "public"."step_advertisement"
    ADD COLUMN "wx_action_url" TEXT;
