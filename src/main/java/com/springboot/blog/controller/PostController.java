package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create blog post rest api
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDTO) {
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }
    // get all post rest api
     @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value="pageNo",defaultValue ="0",required = false) int pageNo, @RequestParam(value = "pageSize",defaultValue ="10" ,required = false) int pageSize
     ){
    return postService.getAllPosts(pageNo,pageSize);

    }

    // get post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPosyById(@PathVariable(name = "id") long id) {
        return  ResponseEntity.ok(postService.getPostById(id));
    }
    // update post by id
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDTO,@PathVariable(name = "id") long id ) {
        PostDto postResponse=postService.updatePost(postDTO,id);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    // delete post rest pai
    @DeleteMapping ("/{id}")
    public ResponseEntity<String > deletePost(@PathVariable(name = "id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully",HttpStatus.OK);

    }
}