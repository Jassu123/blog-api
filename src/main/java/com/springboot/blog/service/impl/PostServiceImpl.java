package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDTO) {
        // convert dto into entity
        Post post = mapToEntity(postDTO);
        Post newPost = postRepository.save(post);
        // convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize) {
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        Page<Post> posts= postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts=posts.getContent();
       List<PostDto> content= listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        return mapToDTO(post);

    }

    @Override
    public PostDto updatePost(PostDto postDTO, long id) {
        // get post by id from the database
        Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
             post.setTitle(postDTO.getTitle());
             post.setContent(postDTO.getContent());
             post.setDescription(postDTO.getDescription());
            Post updatedPost=postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    // convert entity into DTO
    private PostDto mapToDTO(Post post) {
        PostDto postDTO = new PostDto();
        postDTO.setId(post.getId());
        postDTO.setDescription(post.getDescription());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        return postDTO;
    }
    // convert DTO into entity
    private Post mapToEntity(PostDto postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
        post.setTitle(postDTO.getTitle());
        return post;
    }

}
