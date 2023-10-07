package com.stepbook.domain.comment.mapper;

import com.stepbook.interfaces.client.dto.CommentDetailDto;
import com.stepbook.domain.comment.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper extends BaseMapper<CommentEntity> {

    List<CommentDetailDto> findCommentsByCriteria(@Param("bookId") String bookId, @Param("userId") String userId);

    IPage<CommentDetailDto> searchCommentsByCriteria(Page<CommentDetailDto> page, String nickname, String bookName);

    IPage<CommentDetailDto> findCommentsByUser(Page<CommentDetailDto> page, String userId);
}
