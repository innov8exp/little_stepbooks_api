package net.stepbooks.domain.classification.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.classification.entity.ClassificationEntity;
import net.stepbooks.domain.classification.mapper.ClassificationMapper;
import net.stepbooks.domain.classification.service.ClassificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassificationServiceImpl implements ClassificationService {

    private final ClassificationMapper classificationMapper;

    @Override
    public List<ClassificationEntity> findAll() {
        return classificationMapper.selectList(Wrappers.lambdaQuery());
    }

    @Override
    public List<ClassificationEntity> findByBookId(String bookId) {
        return null;
    }

    @Override
    public ClassificationEntity findOne(String id) {
        return classificationMapper.selectById(id);
    }

    @Override
    public void createOne(ClassificationEntity entity) {
        classificationMapper.insert(entity);
    }

    @Override
    public void updateOne(String id, ClassificationEntity updatedEntity) {
        updatedEntity.setId(id);
        classificationMapper.updateById(updatedEntity);
    }

    @Override
    public void deleteOne(String id) {
        classificationMapper.deleteById(id);
    }
}
