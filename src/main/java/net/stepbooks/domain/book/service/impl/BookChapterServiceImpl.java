package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.mapper.BookChapterMapper;
import net.stepbooks.domain.book.service.BookChapterService;
import net.stepbooks.domain.media.service.impl.PrivateFileServiceImpl;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.BookChapterDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookChapterServiceImpl extends ServiceImpl<BookChapterMapper, BookChapter> implements BookChapterService {

    private final BookChapterMapper bookChapterMapper;
    private final PrivateFileServiceImpl privateFileService;


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
    public BookChapterDto getDetailById(String id) {
        BookChapter chapter = getById(id);
        String imgObjectKey = chapter.getImgUrl();
        String audioObjectKey = chapter.getAudioUrl();
        String imgUrl = privateFileService.getUrl(imgObjectKey);
        String audioUrl = privateFileService.getUrl(audioObjectKey);
        chapter.setImgUrl(imgUrl);
        chapter.setAudioUrl(audioUrl);
        BookChapterDto bookChapterDto = BaseAssembler.convert(chapter, BookChapterDto.class);
        bookChapterDto.setImgKey(imgObjectKey);
        bookChapterDto.setAudioKey(audioObjectKey);
        return bookChapterDto;
    }

    @Override
    public void createBookChapter(BookChapter bookChapter) {
        save(bookChapter);
    }
}
