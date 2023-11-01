package net.stepbooks.domain.classification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.classification.mapper.ClassificationMapper;
import net.stepbooks.domain.classification.service.ClassificationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements ClassificationService {
}
