package com.finalproject.hwangjunha_team3.controller;

import com.finalproject.hwangjunha_team3.domain.Post;
import com.finalproject.hwangjunha_team3.domain.Response;
import com.finalproject.hwangjunha_team3.domain.dto.*;
import com.finalproject.hwangjunha_team3.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;


    @PostMapping
    public Response<PostRegisterResponse> post(@RequestBody PostRegisterRequest request, Authentication authentication) {
        log.info("username:{}", authentication.getName());
        PostDto postDto = postService.post(request, authentication.getName());
        return Response.success(new PostRegisterResponse("포스트 등록 완료", postDto.getPostId()));
    }

    @GetMapping("/{postsId}")
    public ResponseEntity<PostDto> findById(@PathVariable Integer postsId) {
        return ResponseEntity.ok().body(postService.findById(postsId));
    }

    @GetMapping
    public Response<Page<PostDto>> getPostList(@PageableDefault(size = 20)
                                               @SortDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> postDtos = postService.getAllPosts(pageable);
        return Response.success(postDtos);
    }

    @DeleteMapping("/{postId}")
    public Response<PostResponse> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success(new PostResponse("포스트 삭제 완료", postId));
    }

    @PutMapping("/{id}")
    public Response<PostResponse> modify(@PathVariable Integer id, @RequestBody ModifyRequest dto, Authentication authentication) {
        Post postEntity = postService.modify(authentication.getName(), id, dto.getTitle(), dto.getBody());
        return Response.success(new PostResponse("포스트 수정 완료", postEntity.getId()));
    }

}
