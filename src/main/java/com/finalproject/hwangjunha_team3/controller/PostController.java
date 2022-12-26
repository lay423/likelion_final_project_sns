package com.finalproject.hwangjunha_team3.controller;

import com.finalproject.hwangjunha_team3.domain.Response;
import com.finalproject.hwangjunha_team3.domain.dto.PostDto;
import com.finalproject.hwangjunha_team3.domain.dto.PostInquireResponse;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterRequest;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterResponse;
import com.finalproject.hwangjunha_team3.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;


    @PostMapping()
    public Response<PostRegisterResponse> post(@RequestBody PostRegisterRequest request, Authentication authentication) {
        log.info("username:{}", authentication.getName());
        return Response.success(new PostRegisterResponse("포스트 등록 완료", postService.post(request).getId()));
    }

    @GetMapping("/{postsId}")
    public Response<PostInquireResponse> findById(@PathVariable long postsId) {
        return Response.success(postService.findById(postsId));
    }

    @GetMapping
    public Response<Page<PostDto>> getPostList(@PageableDefault(size = 20)
                                               @SortDefault(sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> postDtos = postService.getAllPosts(pageable);
        return Response.success(postDtos);
    }
}
