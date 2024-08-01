package net.stepbooks.domain.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.PointsTask;
import net.stepbooks.domain.points.entity.UserPointsTask;
import net.stepbooks.domain.points.enums.PointsTaskType;
import net.stepbooks.domain.points.mapper.UserPointsTaskMapper;
import net.stepbooks.domain.points.service.PointsTaskService;
import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.domain.points.service.UserPointsTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserPointsTaskServiceImpl extends ServiceImpl<UserPointsTaskMapper, UserPointsTask>
        implements UserPointsTaskService {

    private final PointsTaskService pointsTaskService;

    private final UserPointsService userPointsService;

    private void saveUserPointsTask(String userId, String taskId, PointsTask pointsTask) {
        LocalDate now = LocalDate.now();
        UserPointsTask userPointsTask = new UserPointsTask();
        userPointsTask.setUserId(userId);
        userPointsTask.setTaskId(taskId);
        userPointsTask.setPoints(pointsTask.getPoints());
        userPointsTask.setCompleted(true);
        userPointsTask.setCompletedDate(now);
        save(userPointsTask);
        userPointsService.taskFinished(userId, taskId, pointsTask);
    }

    @Transactional
    @Override
    public void finishTask(String userId, String taskId) {
        PointsTask pointsTask = pointsTaskService.getById(taskId);
        if (pointsTask != null) {
            if (PointsTaskType.DAILY.equals(pointsTask.getType())) {
                saveUserPointsTask(userId, taskId, pointsTask);
            } else if (PointsTaskType.SPECIAL.equals(pointsTask.getType())) {
                LambdaQueryWrapper<UserPointsTask> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(UserPointsTask::getUserId, userId);
                wrapper.eq(UserPointsTask::getTaskId, taskId);
                if (!exists(wrapper)) {
                    saveUserPointsTask(userId, taskId, pointsTask);
                }
            }
        }
    }

    @Override
    public void startTask(String userId, String taskId) {

    }


    @Override
    public List<UserPointsTask> todayCompleted(String userId) {
        LocalDate now = LocalDate.now();

        LambdaQueryWrapper<UserPointsTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPointsTask::getUserId, userId);
        wrapper.eq(UserPointsTask::getCompletedDate, now);

        List<UserPointsTask> results = list(wrapper);

        List<PointsTask> currentSpecialTasks = pointsTaskService.currentSpecialTasks();
        if (currentSpecialTasks != null && currentSpecialTasks.size() > 0) {
            List<String> taskIds = currentSpecialTasks.stream().
                    map(PointsTask::getId).collect(Collectors.toList());

            wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserPointsTask::getUserId, userId);
            wrapper.in(UserPointsTask::getTaskId, taskIds);
            wrapper.ne(UserPointsTask::getCompletedDate, now);

            List<UserPointsTask> specialResults = list(wrapper);
            results.addAll(specialResults);
        }

        return results;
    }

}
