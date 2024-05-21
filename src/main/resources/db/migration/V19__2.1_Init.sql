-- 给虚拟产品大类增加"标签"字段，只有parent_id为空的虚拟产品大类才可以设置"标签"
ALTER TABLE STEP_VIRTUAL_CATEGORY
    ADD COLUMN tags VARCHAR(100); -- 音频\课程\活动\播客\训练营，支持用逗号分隔的多个标签
