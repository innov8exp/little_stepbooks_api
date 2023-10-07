package com.stepbook.domain.comment.service.impl;

import com.stepbook.domain.comment.entity.RatingEntity;
import com.stepbook.domain.comment.service.RatingService;
import com.stepbook.domain.comment.mapper.RatingMapper;
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
