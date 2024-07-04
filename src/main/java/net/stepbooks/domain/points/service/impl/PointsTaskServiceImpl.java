package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.PointsTask;
import net.stepbooks.domain.points.mapper.PointsTaskMapper;
import net.stepbooks.domain.points.service.PointsTaskService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointsTaskServiceImpl extends ServiceImpl<PointsTaskMapper, PointsTask>
        implements PointsTaskService {

}
