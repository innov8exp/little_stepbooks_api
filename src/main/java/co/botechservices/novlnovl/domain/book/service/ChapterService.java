package co.botechservices.novlnovl.domain.book.service;

import co.botechservices.novlnovl.domain.book.dto.ChapterCountDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterDto;
import co.botechservices.novlnovl.domain.book.entity.ChapterEntity;
import co.botechservices.novlnovl.infrastructure.enums.UploadType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChapterService {

    List<ChapterDto> getChaptersByBookId(String bookId, String userId);

    ChapterEntity findChapterById(String chapterId);

    void createChapter(ChapterDto dto);

    void updateChapter(String id, ChapterDto dto);

    void updateChapterType(String id, Boolean needPay);

    void deleteChapter(String id);

    IPage<ChapterEntity> findChaptersInPaging(Page<ChapterEntity> page, String bookId);

    void uploadContent(String bookId, UploadType uploadType, MultipartFile file);

    String readChapter(String id);

    List<ChapterCountDto> findCategoryCountsByBookId(String[] bookIds);

    Integer getMaxChapterNumber(String bookId);
}
