package net.stepbooks.domain.exercise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.exercise.entity.Exercise;
import net.stepbooks.domain.exercise.mapper.ExerciseMapper;
import net.stepbooks.domain.exercise.service.ExerciseService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.ExerciseDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl extends ServiceImpl<ExerciseMapper, Exercise> implements ExerciseService {


    @Override
    public void create(Exercise entity) {
        entity.setCreatedAt(LocalDateTime.now());
        this.baseMapper.insert(entity);
    }

    @Override
    public void update(String id, Exercise updatedEntity) {
        this.checkExists(id);
        Exercise tagEntity = this.baseMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, tagEntity, "id", "modifiedAt");
        tagEntity.setModifiedAt(LocalDateTime.now());
        this.baseMapper.updateById(tagEntity);
    }

    @Override
    public void delete(String id) {
        this.checkExists(id);
        this.baseMapper.deleteById(id);
    }

    @Override
    public Exercise getById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public IPage<Exercise> getPage(Page<Exercise> page, ExerciseDto queryDto) {
        LambdaQueryWrapper<Exercise> wrapper = Wrappers.lambdaQuery();
        String courseId = queryDto.getCourseId();
        String nature = queryDto.getNature();
        wrapper.eq(ObjectUtils.isNotEmpty(courseId), Exercise::getCourseId, courseId);
        wrapper.eq(ObjectUtils.isNotEmpty(nature), Exercise::getNature, nature);
        return this.baseMapper.selectPage(page, wrapper);
    }

    private void checkExists(String id) {
        boolean exists = this.baseMapper.exists(Wrappers.<Exercise>lambdaQuery().eq(Exercise::getId, id));
        if (!exists) {
            throw new BusinessException(ErrorCode.EXERCISE_NOT_EXISTS_ERROR);

        }

    }
}
