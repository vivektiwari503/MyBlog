package com.myfirstblog.controller;

import com.myfirstblog.payload.CommentDTO;
import com.myfirstblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId,
                                                    @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }
    //http://localhost:8080/api/posts/{postId}/comments/{id}
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long id,
            @RequestBody CommentDTO commentDTO

    ) {
        CommentDTO dto = commentService.updateComment(postId, id, commentDTO);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value= "postId") Long postId){
        return commentService.getCommentByPostId(postId);

    }
    //http://localhost:8080/api/posts/1/comments/1
    @GetMapping("/posts{postId}/comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value="postId") long postId,
                          @PathVariable(value="id") long commentId){
        CommentDTO commentDTO=commentService.getCommentById(postId,commentId);
        return new ResponseEntity<>(commentDTO,HttpStatus.OK);

    }
    //http://localhost:8080/api/posts/{postId}/comments/{id}
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long id
    ){
        commentService.deleteComment(postId,id);
        return new ResponseEntity<>("comment deleted successfully",HttpStatus.OK);
    }
}