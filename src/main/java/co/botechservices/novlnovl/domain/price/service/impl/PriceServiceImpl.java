package co.botechservices.novlnovl.domain.price.service.impl;

import co.botechservices.novlnovl.domain.price.entity.PriceEntity;
import co.botechservices.novlnovl.domain.price.service.PriceService;
import co.botechservices.novlnovl.infrastructure.mapper.PriceMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceMapper priceMapper;

    @Override
    public PriceEntity getBookChapterPrice(String bookId) {
        return priceMapper.selectOne(Wrappers.<PriceEntity>lambdaQuery().eq(PriceEntity::getBookId, bookId));
    }
}
