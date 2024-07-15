package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.PointsTask;
import net.stepbooks.domain.points.enums.PointsTaskType;
import net.stepbooks.domain.points.mapper.PointsTaskMapper;
import net.stepbooks.domain.points.service.PointsTaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PointsTaskServiceImpl extends ServiceImpl<PointsTaskMapper, PointsTask>
        implements PointsTaskService {

    @Override
    public List<PointsTask> currentSpecialTasks() {
        LocalDate now = LocalDate.now();
        LambdaQueryWrapper<PointsTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PointsTask::isActive, true);
        wrapper.eq(PointsTask::getType, PointsTaskType.SPECIAL);
        wrapper.le(PointsTask::getStartDate, now);
        wrapper.ge(PointsTask::getEndDate, now);
        return list(wrapper);
    }
}
