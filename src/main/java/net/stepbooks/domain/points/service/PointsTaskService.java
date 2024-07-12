package net.stepbooks.domain.points.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.points.entity.PointsTask;

import java.util.List;

public interface PointsTaskService extends IService<PointsTask> {

    /**
     * 当前有效的特殊积分任务
     *
     * @return
     */
    List<PointsTask> currentSpecialTasks();

}
