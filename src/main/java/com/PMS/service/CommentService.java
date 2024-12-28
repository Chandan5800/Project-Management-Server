package com.PMS.service;

import java.util.List;

import com.PMS.model.Comment;

public interface CommentService {
    Comment createComment(Long issueId, Long userId, String content) throws Exception;

    void  deleteComment(Long commentId, Long userId) throws Exception;

    List<Comment> findCommentsByIssueId(Long issueId);
}
