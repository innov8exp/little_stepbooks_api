package net.stepbooks.domain.comment.service.impl;

import net.stepbooks.domain.comment.entity.RatingEntity;
import net.stepbooks.domain.comment.service.RatingService;
import net.stepbooks.domain.comment.mapper.RatingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper ratingMapper;

    @Override
    public void createRating(RatingEntity ratingEntity) {
        ratingEntity.setCreatedAt(LocalDateTime.now());
        ratingMapper.insert(ratingEntity);
    }
}
