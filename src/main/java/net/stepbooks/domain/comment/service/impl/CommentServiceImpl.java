package net.stepbooks.domain.comment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.comment.entity.CommentEntity;
import net.stepbooks.domain.comment.mapper.CommentMapper;
import net.stepbooks.domain.comment.service.CommentService;
import net.stepbooks.application.dto.client.CommentDetailDto;
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
