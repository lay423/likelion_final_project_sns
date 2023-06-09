package com.finalproject.hwangjunha_team3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.hwangjunha_team3.configuration.EncrypterConfig;
import com.finalproject.hwangjunha_team3.domain.Post;
import com.finalproject.hwangjunha_team3.domain.dto.*;
import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import com.finalproject.hwangjunha_team3.exceptionManager.HospitalReviewAppException;
import com.finalproject.hwangjunha_team3.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
class PostControllerTest {
    //
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    EncrypterConfig encoderConfig;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 조회 성공")
    void post_read_success() throws Exception {

        PostDto postEntity = PostDto.builder()
                .id(1)
                .title("TITLE")
                .body("BODY")
                .userName("junha")
                .createdAt(now())
                .build();

        when(postService.findById(any()))
                .thenReturn(postEntity);

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.body").exists());
    }


    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("pageable 파라미터 검증")
    void evaluates_pageable_parameter() throws Exception {

        mockMvc.perform(get("/api/v1/posts")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "createdAt,desc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).getAllPosts(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(0, pageable.getPageNumber());
        assertEquals(3, pageable.getPageSize());
        assertEquals(Sort.by("createdAt", "desc"), pageable.withSort(Sort.by("createdAt", "desc")).getSort());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 작성 성공")
    void post_success() throws Exception {

        PostRegisterRequest postRequest = PostRegisterRequest.builder()
                .title("TITLE")
                .body("BODY")
                .build();

        when(postService.post(any(), any()))
                .thenReturn(PostDto.builder()
                        .id(0)
                        .build());

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
        ;
    }

    //
    @Test
    @WithAnonymousUser // 인증 된지 않은 상태
    @DisplayName("포스트 작성 실패(1) : 인증 실패")
    void post_fail1() throws Exception {

        PostRegisterRequest postRequest = PostRegisterRequest.builder()
                .title("TITLE")
                .body("BODY")
                .build();

        when(postService.post(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 성공")
    void delete_success() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser // 인증 된지 않은 상태
    @DisplayName("포스트 삭제 실패(1) : 인증 실패")
    void delete_fail1() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 실패(2) : 포스트 내용 불일치")
    void delete_fail2() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, ""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 실패(3) : 작성자 불일치")
    void delete_fail3() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 삭제 실패(4) : 데이터베이스 에러")
    void delete_fail4() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.DATABASE_ERROR, ""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 성공")
    void modify_success() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("TITLE_MODIFY")
                .body("BODY_MODIFY")
                .build();

        Post postEntity = Post.builder()
                .id(1)
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenReturn(postEntity);

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser // 인증 되지 않은 상태
    @DisplayName("포스트 수정 실패(1) : 인증 실패")
    void modify_fail1() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("TITLE_MODIFY")
                .body("BODY_MODIFY")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 실패(2) : 포스트 내용 불일치")
    void modify_fail2() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("TITLE_MODIFY")
                .body("BODY_MODIFY")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, ""));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 실패(3) : 작성자 불일치")
    void modify_fail3() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("TITLE_MODIFY")
                .body("BODY_MODIFY")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("포스트 수정 실패(4) : 데이터베이스 에러")
    void modify_fail4() throws Exception {

        ModifyRequest modifyRequest = ModifyRequest.builder()
                .title("TITLE_MODIFY")
                .body("BODY_MODIFY")
                .build();

        when(postService.modify(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.DATABASE_ERROR, ""));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 작성 성공")
    void comment_success() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment test")
                .build();

        when(postService.comment(any(), any(), any()))
                .thenReturn(CommentResponse.builder()
                        .id(0)
                        .comment("comment test")
                        .userName("junha")
                        .postId(0)
                        .createdAt(now())
                        .build());

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 작성 실패(1) - 로그인 하지 않은 경우")
    void comment_fail1() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment test")
                .build();

        when(postService.comment(any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 작성 실패(2) - 게시물이 존재하지 않는 경우")
    void comment_fail2() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment test")
                .build();

        when(postService.comment(any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 성공")
    void comment_modify_success() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment modify test")
                .build();

        when(postService.modifyComment(any(), any(), any(), any()))
                .thenReturn(CommentModifyResponse.builder()
                        .id(0)
                        .comment("comment modify test")
                        .userName("junha")
                        .postId(0)
                        .createdAt(now())
                        .lastModifiedAt(now())
                        .build());

        mockMvc.perform(put("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andExpect(jsonPath("$.result.lastModifiedAt").exists())
        ;

    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(1) : 인증 실패")
    void comment_modify_fail1() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment modify test")
                .build();

        when(postService.modifyComment(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(put("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(2) : 댓글 불일치")
    void comment_modify_fail2() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment modify test")
                .build();

        when(postService.modifyComment(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.COMMENT_NOT_FOUND, ""));

        mockMvc.perform(put("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.COMMENT_NOT_FOUND.getStatus().value()))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(3) : 작성자 불일치")
    void comment_modify_fail3() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment modify test")
                .build();

        when(postService.modifyComment(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, ""));

        mockMvc.perform(put("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getStatus().value()))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 실패(4) : 데이터베이스 에러")
    void comment_modify_fail4() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .comment("comment modify test")
                .build();

        when(postService.modifyComment(any(), any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.DATABASE_ERROR, ""));

        mockMvc.perform(put("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 성공")
    void comment_delete_success() throws Exception {

        mockMvc.perform(delete("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.id").exists())
        ;
    }
    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(1) : 인증 실패")
    void comment_delete_fail1() throws Exception {

        when(postService.deleteComment(any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(delete("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(2) : 댓글 불일치")
    void comment_delete_fail2() throws Exception {

        when(postService.deleteComment(any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.COMMENT_NOT_FOUND, ""));

        mockMvc.perform(delete("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.COMMENT_NOT_FOUND.getStatus().value()))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(3) : 작성자 불일치")
    void comment_delete_fail3() throws Exception {

        when(postService.deleteComment(any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, ""));

        mockMvc.perform(delete("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getStatus().value()))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 실패(4) : 데이터베이스 에러")
    void comment_delete_fail4() throws Exception {

        when(postService.deleteComment(any(), any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.DATABASE_ERROR, ""));

        mockMvc.perform(delete("/api/v1//posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()))
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요 누르기 성공")
    void like_success() throws Exception {

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요 누르기 실패(1) - 로그인 하지 않은 경우")
    void like_fail1() throws Exception {

        when(postService.like(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요 누르기 실패(2) - 해당 Post가 없는 경우")
    void like_fail2() throws Exception {

        when(postService.like(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));

    }

    @Test
    @WithMockUser
    @DisplayName("마이피드 조회 성공")
    void myFeed_success() throws Exception {

        when(postService.myFeed(any(), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithMockUser
    @DisplayName("마이피드 조회 실패(1) - 로그인 하지 않은 경우")
    void myFeed_fail1() throws Exception {

        when(postService.myFeed(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(get("/api/v1/posts/my")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }



}