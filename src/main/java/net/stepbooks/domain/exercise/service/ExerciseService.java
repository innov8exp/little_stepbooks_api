package net.stepbooks.domain.exercise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.exercise.entity.Exercise;
import net.stepbooks.interfaces.admin.dto.ExerciseDto;

public interface ExerciseService extends IService<Exercise> {
    void create(Exercise entity);

    void update(String id, Exercise updatedEntity);

    void delete(String id);

    Exercise getById(String id);

    IPage<Exercise> getPage(Page<Exercise> page, ExerciseDto queryDto);
}
