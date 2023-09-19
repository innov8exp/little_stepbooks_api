package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.book.dto.ChapterCountDto;
import co.botechservices.novlnovl.domain.book.entity.ChapterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChapterMapper extends BaseMapper<ChapterEntity> {

    IPage<ChapterEntity> findChaptersInPaging(Page<ChapterEntity> page, @Param("bookId") String bookId);

    List<ChapterCountDto> findChapterCountByBooks(List<String> bookIds);
}
