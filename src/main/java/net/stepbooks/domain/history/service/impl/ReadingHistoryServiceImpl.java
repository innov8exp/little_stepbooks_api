package net.stepbooks.domain.history.service.impl;

import net.stepbooks.interfaces.client.dto.BookDetailDto;
import net.stepbooks.interfaces.client.dto.ChapterDto;
import net.stepbooks.interfaces.client.dto.ChapterWithHistoryDto;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.domain.order.service.ConsumptionService;
import net.stepbooks.domain.history.mapper.ReadingHistoryMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadingHistoryServiceImpl implements ReadingHistoryService {

    private final ReadingHistoryMapper readingHistoryMapper;
    private final ConsumptionService consumptionService;

    @Override
    public void createOrUpdateReadingHistory(ReadingHistoryEntity readingHistoryEntity) {
        ReadingHistoryEntity historyEntity = readingHistoryMapper.selectOne(Wrappers.<ReadingHistoryEntity>lambdaQuery()
                .eq(ReadingHistoryEntity::getBookId, readingHistoryEntity.getBookId())
                .eq(ReadingHistoryEntity::getUserId, readingHistoryEntity.getUserId()));
        if (ObjectUtils.isEmpty(historyEntity)) {
            readingHistoryEntity.setCreatedAt(LocalDateTime.now());
            readingHistoryMapper.insert(readingHistoryEntity);
            return;
        }
        readingHistoryEntity.setId(historyEntity.getId());
        readingHistoryEntity.setModifiedAt(LocalDateTime.now());
        readingHistoryMapper.updateById(readingHistoryEntity);
    }

    @Override
    public List<ChapterWithHistoryDto> getReadingHistory(String bookId, String userId) {
        //TODO: implement me
        return null;
    }

    @Override
    public IPage<BookDetailDto> getUserReadBooks(Page<BookDetailDto> page, String userId) {
        return readingHistoryMapper.findUserReadBooks(page, userId);
    }
}
