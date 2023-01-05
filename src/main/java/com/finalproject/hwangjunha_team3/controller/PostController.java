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
    public Response<PostResponse> post(@RequestBody PostRegisterRequest request, Authentication authentication) {
        log.info("username:{}", authentication.getName());
        PostDto postDto = postService.post(request, authentication.getName());
        return Response.success(new PostResponse("포스트 등록 완료", postDto.getId()));
    }

    @GetMapping("/{postsId}")
    public Response<PostDto> findById(@PathVariable Integer postsId) {
        return Response.success(postService.findById(postsId));
    }

    @GetMapping
    public Response<Page<PostDto>> getPostList(@PageableDefault(size = 20)
                                               @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
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

    @PostMapping("/{id}/comments")
    public Response<CommentResponse> comment(@PathVariable Integer id, @RequestBody CommentRequest commentRequest, Authentication authentication) {
        log.info("comment:{}, user:{}, id:{}", commentRequest.getComment(), authentication.getName(), id);
        return Response.success(postService.comment(commentRequest, authentication.getName(), id));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public Response<CommentModifyResponse> modifyComment(@PathVariable Integer postId, @PathVariable Integer commentId, @RequestBody CommentRequest commentRequest, Authentication authentication) {
        return Response.success(postService.modifyComment(postId, commentId, commentRequest, authentication.getName()));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<CommentDeleteResponse> deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId, Authentication authentication) {
        postService.deleteComment(authentication.getName(), postId, commentId);
        return Response.success(new CommentDeleteResponse("댓글 삭제 완료", commentId));
    }

    @GetMapping("/{postId}/coments")
    public Response<Page<CommentResponse>> getCommentList(@PageableDefault(size = 20)
                                                          @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                          @PathVariable Integer postId) {
        return Response.success(postService.getAllComments(pageable, postId));
    }

    @PostMapping("/{postId}/likes")
    public Response<String> like(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success("좋아요를 눌렀습니다.");
    }
}
