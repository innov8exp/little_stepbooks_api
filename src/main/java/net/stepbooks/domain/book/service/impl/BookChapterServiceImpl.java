package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.mapper.BookChapterMapper;
import net.stepbooks.domain.book.service.BookChapterService;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.domain.media.service.impl.PrivateFileServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookChapterServiceImpl extends ServiceImpl<BookChapterMapper, BookChapter> implements BookChapterService {

    private final BookChapterMapper bookChapterMapper;
    private final PrivateFileServiceImpl privateFileService;
    private final MediaService mediaService;


    @Override
    public List<BookChapter> getBookChapters(String bookId) {
        return bookChapterMapper.selectList(Wrappers.<BookChapter>lambdaQuery()
                .eq(BookChapter::getBookId, bookId)
                .orderBy(true, true, BookChapter::getChapterNo));
    }

    @Override
    public Long getMaxChapterNo(String bookId) {
        return bookChapterMapper.getMaxChapterNoByBookId(bookId);
    }

    @Override
    public BookChapter getDetailById(String id) {
        BookChapter chapter = getById(id);
        String imgObjectKey = chapter.getImgUrl();
        String audioObjectKey = chapter.getAudioUrl();
//        Media imgMedia = mediaService.getById(imgId);
//        if (ObjectUtils.isEmpty(imgMedia)) {
//            throw new BusinessException(ErrorCode.MEDIA_NOT_FOUND);
//        }
//        Media audioMedia = mediaService.getById(audioId);
//        if (ObjectUtils.isEmpty(audioMedia)) {
//            throw new BusinessException(ErrorCode.MEDIA_NOT_FOUND);
//        }
        String imgUrl = privateFileService.getUrl(imgObjectKey);
        String audioUrl = privateFileService.getUrl(audioObjectKey);
        chapter.setImgUrl(imgUrl);
        chapter.setAudioUrl(audioUrl);
        return chapter;
    }
}
