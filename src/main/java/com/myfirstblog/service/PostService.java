package com.myfirstblog.service;

import com.myfirstblog.payload.PostDTO;
import com.myfirstblog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDTO getPostById(Long id);

    PostDTO updatePost(PostDTO postDto, long id);

    void deletePostById(long id);
}
