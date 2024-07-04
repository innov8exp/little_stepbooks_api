package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.UserPointsTask;
import net.stepbooks.domain.points.mapper.UserPointsTaskMapper;
import net.stepbooks.domain.points.service.UserPointsTaskService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPointsTaskServiceImpl extends ServiceImpl<UserPointsTaskMapper, UserPointsTask>
        implements UserPointsTaskService {

}
