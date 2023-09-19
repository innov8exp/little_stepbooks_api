package co.botechservices.novlnovl.domain.history.service.impl;

import co.botechservices.novlnovl.domain.book.dto.BookDetailDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterWithHistoryDto;
import co.botechservices.novlnovl.domain.book.service.ChapterService;
import co.botechservices.novlnovl.domain.history.entity.ReadingHistoryEntity;
import co.botechservices.novlnovl.domain.history.service.ReadingHistoryService;
import co.botechservices.novlnovl.domain.order.service.ConsumptionService;
import co.botechservices.novlnovl.infrastructure.mapper.ReadingHistoryMapper;
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
    private final ChapterService chapterService;
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
        List<ChapterDto> chapters = chapterService.getChaptersByBookId(bookId, userId);
        ChapterWithHistoryDto chapterWithHistoryDto = readingHistoryMapper.findBookLastChapterRecordByUser(bookId, userId);

        return chapters.stream().map(chapterDto -> {
            ChapterWithHistoryDto historyDto = ChapterWithHistoryDto.builder()
                    .paragraphNumber(1L)
                    .build();
            if (chapterWithHistoryDto != null && chapterDto.getId().equals(chapterWithHistoryDto.getId())) {
                historyDto = chapterWithHistoryDto;
                historyDto.setIsLastReadChapter(true);
            } else {
                BeanUtils.copyProperties(chapterDto, historyDto);
            }
            historyDto.setUnlocked(chapterDto.getUnlocked());
            return historyDto;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<BookDetailDto> getUserReadBooks(Page<BookDetailDto> page, String userId) {
        return readingHistoryMapper.findUserReadBooks(page, userId);
    }
}
