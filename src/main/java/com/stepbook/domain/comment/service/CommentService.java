package com.stepbook.domain.comment.service;

import com.stepbook.interfaces.client.dto.CommentDetailDto;
import com.stepbook.domain.comment.entity.CommentEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface CommentService {

    void createComment(CommentEntity commentEntity);

    void deleteComment(String id);

    List<CommentEntity> findBookComments(String bookId);

    List<CommentDetailDto> findComments(String bookId, String userId);

    IPage<CommentDetailDto> findCommentsByCriteria(Page<CommentDetailDto> page, String nickname, String bookName);

    IPage<CommentDetailDto> findCommentsByUser(Page<CommentDetailDto> page, String userId);
}
