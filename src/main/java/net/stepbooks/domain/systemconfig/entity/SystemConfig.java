package net.stepbooks.domain.systemconfig.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("STEP_SYSTEM_CONFIG")
public class SystemConfig {
    private String id;
    private String k;
    private String v;
}
