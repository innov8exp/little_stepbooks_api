package com.stepbook.domain.library.service.impl;

import com.stepbook.domain.book.entity.BookEntity;
import com.stepbook.domain.library.entity.FavoriteEntity;
import com.stepbook.domain.library.service.FavoriteService;
import com.stepbook.infrastructure.mapper.FavoriteMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public FavoriteEntity findBookInFavorite(String bookId, String userId) {
        return favoriteMapper.selectOne(Wrappers.<FavoriteEntity>lambdaQuery()
                .eq(FavoriteEntity::getBookId, bookId)
                .eq(FavoriteEntity::getUserId, userId));
    }

    @Override
    public int addBookToFavorite(String bookId, String userId) {
        FavoriteEntity bookInFavorite = findBookInFavorite(bookId, userId);
        if (bookInFavorite != null) {
            return 0;
        }
        FavoriteEntity favoriteEntity = FavoriteEntity.builder().bookId(bookId).userId(userId).build();
        return favoriteMapper.insert(favoriteEntity);
    }

    @Override
    public int removeBooksFromFavorite(List<String> bookIds, String userId) {
        return favoriteMapper.delete(Wrappers.<FavoriteEntity>lambdaQuery()
                .and(q -> q.in(FavoriteEntity::getBookId, bookIds)
                        .eq(FavoriteEntity::getUserId, userId)));
    }

    @Override
    public List<BookEntity> listFavoriteBooks(String userId) {
        return favoriteMapper.findBooksFromFavoriteByUserId(userId);
    }

    @Transactional
    @Override
    public void setTopBooksFromFavorites(List<String> bookIds, String userId) {
        List<FavoriteEntity> favoriteEntities = favoriteMapper.selectList(Wrappers.<FavoriteEntity>lambdaQuery()
                .eq(FavoriteEntity::getUserId, userId));
        favoriteEntities.forEach(favoriteEntity -> {
            favoriteEntity.setSortIndex(favoriteEntity.getSortIndex() + bookIds.size());
            favoriteMapper.updateById(favoriteEntity);
        });
        List<FavoriteEntity> topFavoriteEntities = favoriteMapper.selectList(Wrappers.<FavoriteEntity>lambdaQuery()
                .eq(FavoriteEntity::getUserId, userId)
                .in(FavoriteEntity::getBookId, bookIds)
                .orderByAsc(FavoriteEntity::getSortIndex));
        for (int i = 0; i < topFavoriteEntities.size(); i++) {
            FavoriteEntity favoriteEntity = topFavoriteEntities.get(i);
            favoriteEntity.setSortIndex(i);
            favoriteMapper.updateById(favoriteEntity);
        }
    }
}
