package com.myfirstblog.service.impl;

import com.myfirstblog.entities.Post;
import com.myfirstblog.exception.ResourceNotFoundException;
import com.myfirstblog.payload.PostDTO;
import com.myfirstblog.payload.PostResponse;
import com.myfirstblog.repositories.PostRepository;
import com.myfirstblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {//it acts like @autowired and we right click & click on generate & constructor

    private PostRepository postRepository;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }


    @Override
    public PostDTO createPost(PostDTO postDto) {
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);
        PostDTO dto = mapToDto(newPost);
        return dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {//pagination
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending()//ternary operator
                : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> content = postRepository.findAll(pageable);
        List<Post> posts = content.getContent();

        List<PostDTO> postDTOS=posts.stream().map(post->mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse= new PostResponse();
        postResponse.setContent(postDTOS);
        postResponse.setPageNo(content.getNumber());
        postResponse.setPageSize(content.getSize());
        postResponse.setTotalElements(content.getTotalElements());
        postResponse.setTotalPages(content.getTotalPages());
        postResponse.setLast(content.isLast());

        return postResponse;
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id)
        );
        return mapToDto(post);

    }

    @Override
    public PostDTO updatePost(PostDTO postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id)//that specific exception will go to GolbalExceptionHandler
                //due to we use @ExceptionHandler(ResourceNotFoundException.class) in GolbalExceptionHandler
        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost=postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id)
        );
        postRepository.deleteById(id);
    }

    PostDTO mapToDto(Post post) {
        PostDTO postDTO = mapper.map(post, PostDTO.class);//we use MOdelMapper to convert mapToDto
        return postDTO;

//        PostDTO dto = new PostDTO();//this is traditional way to convert mapToDto
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());
//        return dto;

    }

    Post mapToEntity(PostDTO dto) {
        Post post = mapper.map(dto, Post.class);//we use MOdelMapper to convert mapToDto
        return post;
//        Post post = new Post();//this is traditional way to convert mapToDto
//        post.setTitle(dto.getTitle());
//        post.setDescription(dto.getDescription());
//        post.setContent(dto.getContent());
//        return post;
    }
}
