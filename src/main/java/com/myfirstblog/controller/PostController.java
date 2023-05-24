package com.myfirstblog.controller;

import com.myfirstblog.payload.PostDTO;
import com.myfirstblog.payload.PostResponse;
import com.myfirstblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //http://localhost:8080/api/posts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult result) {
        System.out.print(100);
        if(result.hasErrors()){ //it capture error in validations/here we are returning two things dto and validations error
            //so thats why we use generic(?)
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDTO dto = postService.createPost(postDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    //http://localhost:8080/api/posts?pageNo=0&pageSize=10
    //http://localhost:8080/api/posts?pageNo=0&pageSize=10&sortBy=title&sortDir=asc
    @GetMapping
    public PostResponse getAllPosts(//pagination & sorting
                                    @RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
                                    @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                    @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                    @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);

    }
    //http://localhost:8080/api/posts/1
    @GetMapping("{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long id){
        return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);
    }
    //http://localhost:8080/api/posts/1
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDto,@PathVariable("id") long id){
        PostDTO postResponse= postService.updatePost(postDto,id);
        return ResponseEntity.ok(postResponse);
    }
    //http://localhost:8080/api/posts/1
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<String>("Post is deleted",HttpStatus.OK);
    }
}