-- 系统配置表
create TABLE STEP_SYSTEM_CONFIG
(
    id VARCHAR(100) NOT NULL PRIMARY KEY, -- 主键ID
    k  VARCHAR(100) NOT NULL,             -- 系统配置属性
    v  VARCHAR(100) NOT NULL              -- 系统配置值
);
