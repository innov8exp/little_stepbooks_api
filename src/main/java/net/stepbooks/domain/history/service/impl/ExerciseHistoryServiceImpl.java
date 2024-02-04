package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.ExerciseHistory;
import net.stepbooks.domain.history.mapper.ExerciseHistoryMapper;
import net.stepbooks.domain.history.service.ExerciseHistoryService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExerciseHistoryServiceImpl extends ServiceImpl<ExerciseHistoryMapper, ExerciseHistory>
        implements ExerciseHistoryService {

    private final ExerciseHistoryMapper exerciseHistoryMapper;

    @Override
    public IPage<ExerciseHistory> getPage(Page<ExerciseHistory> page, String userId, String courseId) {
        LambdaQueryWrapper<ExerciseHistory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(userId), ExerciseHistory::getUserId, userId);
        wrapper.eq(ObjectUtils.isNotEmpty(courseId), ExerciseHistory::getCourseId, courseId);
        return this.baseMapper.selectPage(page, wrapper);
    }

    @Override
    public void submit(String exerciseId, String courseId, String userId, int score) {

        ExerciseHistory exerciseHistory = exerciseHistoryMapper.selectOne(Wrappers.<ExerciseHistory>lambdaQuery()
                .eq(ExerciseHistory::getExerciseId, exerciseId)
                .eq(ExerciseHistory::getCourseId, courseId)
                .eq(ExerciseHistory::getUserId, userId));
        if (exerciseHistory == null) {
            exerciseHistory = new ExerciseHistory();
            exerciseHistory.setExerciseId(exerciseId);
            exerciseHistory.setCourseId(courseId);
            exerciseHistory.setUserId(userId);
            exerciseHistory.setScore(score);
            save(exerciseHistory);
        } else if (exerciseHistory.getScore() < score) {
            exerciseHistory.setScore(score);
            updateById(exerciseHistory);
        } else {
            //do nothing
        }
    }

}
