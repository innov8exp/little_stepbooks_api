package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.history.entity.ExerciseHistory;

public interface ExerciseHistoryService extends IService<ExerciseHistory> {

    /**
     * 分页数据
     */
    IPage<ExerciseHistory> getPage(Page<ExerciseHistory> page, String userId, String courseId);


    void submit(String exerciseId, String courseId, String userId, int score);
}
