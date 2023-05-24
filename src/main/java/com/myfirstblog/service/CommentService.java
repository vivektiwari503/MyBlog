package com.myfirstblog.service;

import com.myfirstblog.payload.CommentDTO;

import java.util.List;

public interface CommentService {

    public CommentDTO createComment(long postId,CommentDTO commentDTO);

    CommentDTO updateComment(long postId, long id, CommentDTO commentDTO);
    List<CommentDTO> getCommentByPostId(long postId);

    CommentDTO getCommentById(long postId, long commentId);

    void deleteComment(long postId, long id);
}
