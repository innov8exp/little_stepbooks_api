package net.stepbooks.domain.systemconfig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.systemconfig.entity.SystemConfig;
import net.stepbooks.domain.systemconfig.mapper.SystemConfigMapper;
import net.stepbooks.domain.systemconfig.service.SystemConfigService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
        implements SystemConfigService {

}
