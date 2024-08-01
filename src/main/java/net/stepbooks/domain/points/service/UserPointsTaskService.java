package net.stepbooks.domain.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.points.entity.UserPointsTask;

import java.util.List;

public interface UserPointsTaskService extends IService<UserPointsTask> {

    void finishTask(String userId, String taskId);

    void startTask(String userId, String taskId);

    List<UserPointsTask> todayCompleted(String userId);

}
