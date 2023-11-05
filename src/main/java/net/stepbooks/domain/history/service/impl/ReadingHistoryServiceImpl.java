package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import net.stepbooks.application.dto.client.ChapterWithHistoryDto;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import net.stepbooks.domain.history.mapper.ReadingHistoryMapper;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingHistoryServiceImpl implements ReadingHistoryService {

    private final ReadingHistoryMapper readingHistoryMapper;

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
        //todo: implement me
        return null;
    }

    @Override
    public IPage<Book> getUserReadBooks(Page<Book> page, String userId) {
        return readingHistoryMapper.findUserReadBooks(page, userId);
    }
}
