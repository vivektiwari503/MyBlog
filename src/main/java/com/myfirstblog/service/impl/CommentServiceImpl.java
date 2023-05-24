package com.myfirstblog.service.impl;

import com.myfirstblog.entities.Comment;
import com.myfirstblog.entities.Post;
import com.myfirstblog.exception.BlogAPIException;
import com.myfirstblog.exception.ResourceNotFoundException;
import com.myfirstblog.payload.CommentDTO;
import com.myfirstblog.repositories.CommentRepository;
import com.myfirstblog.repositories.PostRepository;
import com.myfirstblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {


        Post post=postRepository.findById(postId).orElseThrow(//first check does the post exist for id
                ()->new ResourceNotFoundException("Post","id",postId)
        );
        Comment comment = mapToEntity(commentDTO);
        comment.setPost(post);//here we are saving comment for post that's why we set post here like we define in Comment entity
        //private Post post
        Comment newComment=commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public CommentDTO updateComment(long postId, long id, CommentDTO commentDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", postId)
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id)
        );
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());

        Comment updatedComment= commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public List<CommentDTO> getCommentByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", postId)
        );
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", postId)
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id)
        );
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        commentRepository.deleteById(comment.getId());
    }

    CommentDTO mapToDto(Comment newComment){
        CommentDTO commentDTO= new CommentDTO();
        commentDTO.setId(newComment.getId());
        commentDTO.setName(newComment.getName());
        commentDTO.setEmail(newComment.getEmail());
        commentDTO.setBody(newComment.getBody());
        return commentDTO;

    }
    Comment mapToEntity(CommentDTO commentDTO){
        Comment comment=new Comment();
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());
        return comment;
    }
}
