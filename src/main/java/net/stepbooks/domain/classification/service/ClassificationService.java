package net.stepbooks.domain.classification.service;

import net.stepbooks.domain.classification.entity.ClassificationEntity;

import java.util.List;

public interface ClassificationService {

    List<ClassificationEntity> findAll();

    List<ClassificationEntity> findByBookId(String bookId);

    ClassificationEntity findOne(String id);

    void createOne(ClassificationEntity entity);

    void updateOne(String id, ClassificationEntity updatedEntity);

    void deleteOne(String id);
}
