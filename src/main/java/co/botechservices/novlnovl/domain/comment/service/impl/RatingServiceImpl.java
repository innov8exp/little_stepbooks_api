package co.botechservices.novlnovl.domain.comment.service.impl;

import co.botechservices.novlnovl.domain.comment.entity.RatingEntity;
import co.botechservices.novlnovl.domain.comment.service.RatingService;
import co.botechservices.novlnovl.infrastructure.mapper.RatingMapper;
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
