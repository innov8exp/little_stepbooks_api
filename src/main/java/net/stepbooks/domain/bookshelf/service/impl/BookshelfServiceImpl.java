package net.stepbooks.domain.bookshelf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.advertisement.mapper.RecommendMapper;
import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.bookshelf.entity.BookshelfEntity;
import net.stepbooks.domain.bookshelf.mapper.BookshelfMapper;
import net.stepbooks.domain.bookshelf.service.BookshelfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookshelfServiceImpl implements BookshelfService {

    private final BookshelfMapper bookshelfMapper;
    private final RecommendMapper recommendMapper;

    @Override
    public BookshelfEntity findBookInBookshelf(String bookId, String userId) {
        return bookshelfMapper.selectOne(Wrappers.<BookshelfEntity>lambdaQuery()
                .eq(BookshelfEntity::getBookId, bookId)
                .eq(BookshelfEntity::getUserId, userId));
    }

    @Override
    public int addBookToBookshelf(String bookId, String userId) {
        BookshelfEntity bookInBookshelf = findBookInBookshelf(bookId, userId);
        if (bookInBookshelf != null) {
            return 0;
        }
        BookshelfEntity bookshelfEntity = BookshelfEntity.builder().bookId(bookId).userId(userId).build();
        return bookshelfMapper.insert(bookshelfEntity);
    }

    @Override
    public int removeBooksFromBookshelf(List<String> bookIds, String userId) {
        return bookshelfMapper.delete(Wrappers.<BookshelfEntity>lambdaQuery()
                .and(q -> q.in(BookshelfEntity::getBookId, bookIds)
                        .eq(BookshelfEntity::getUserId, userId)));
    }

    @Override
    public List<BookEntity> listBooksInBookshelf(String userId) {
        //        if (ObjectUtils.isEmpty(books)) {
//            return recommendMapper.findDefaultRecommendBooks();
//        }
        return bookshelfMapper.findBooksInBookshelfByUser(userId);
    }

    @Transactional
    @Override
    public void setTopBooksFromBookshelf(List<String> bookIds, String userId) {
        List<BookshelfEntity> bookshelfEntities = bookshelfMapper.selectList(Wrappers.<BookshelfEntity>lambdaQuery()
                .eq(BookshelfEntity::getUserId, userId));
        bookshelfEntities.forEach(bookshelfEntity -> {
            bookshelfEntity.setSortIndex(bookshelfEntity.getSortIndex() + bookIds.size());
            bookshelfMapper.updateById(bookshelfEntity);
        });
        List<BookshelfEntity> topFavoriteEntities = bookshelfMapper.selectList(Wrappers.<BookshelfEntity>lambdaQuery()
                .eq(BookshelfEntity::getUserId, userId)
                .in(BookshelfEntity::getBookId, bookIds)
                .orderByAsc(BookshelfEntity::getSortIndex));
        for (int i = 0; i < topFavoriteEntities.size(); i++) {
            BookshelfEntity bookshelfEntity = topFavoriteEntities.get(i);
            bookshelfEntity.setSortIndex(i);
            bookshelfMapper.updateById(bookshelfEntity);
        }
    }
}
