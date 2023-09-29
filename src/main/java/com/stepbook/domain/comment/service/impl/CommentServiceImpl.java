package com.stepbook.domain.comment.service.impl;

import com.stepbook.domain.comment.dto.CommentDetailDto;
import com.stepbook.domain.comment.entity.CommentEntity;
import com.stepbook.domain.comment.service.CommentService;
import com.stepbook.infrastructure.mapper.CommentMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public void createComment(CommentEntity commentEntity) {
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(commentEntity);
    }

    @Override
    public void deleteComment(String id) {
        commentMapper.deleteById(id);
    }

    @Override
    public List<CommentEntity> findBookComments(String bookId) {
        return commentMapper.selectList(Wrappers.<CommentEntity>lambdaQuery()
                .eq(CommentEntity::getBookId, bookId));
    }

    @Override
    public List<CommentDetailDto> findComments(String bookId, String userId) {
        return commentMapper.findCommentsByCriteria(bookId, userId);
    }

    @Override
    public IPage<CommentDetailDto> findCommentsByCriteria(Page<CommentDetailDto> page, String nickname, String bookName) {
        return commentMapper.searchCommentsByCriteria(page, nickname, bookName);
    }

    @Override
    public IPage<CommentDetailDto> findCommentsByUser(Page<CommentDetailDto> page, String userId) {
        return commentMapper.findCommentsByUser(page, userId);
    }


}
