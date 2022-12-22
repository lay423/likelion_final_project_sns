package com.finalproject.hwangjunha_team3.controller;

import com.finalproject.hwangjunha_team3.domain.Response;
import com.finalproject.hwangjunha_team3.domain.dto.PostInquireResponse;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterRequest;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterResponse;
import com.finalproject.hwangjunha_team3.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Response<PostRegisterResponse> post(@RequestBody PostRegisterRequest request) {
        return Response.success(postService.post(request));
    }

    @GetMapping("/posts/{postsId}")
    public Response<PostInquireResponse> findById(@PathVariable long postsId) {
        return Response.success(postService.findById(postsId));
    }
}
