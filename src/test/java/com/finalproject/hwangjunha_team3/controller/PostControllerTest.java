package com.finalproject.hwangjunha_team3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.hwangjunha_team3.configuration.EncrypterConfig;
import com.finalproject.hwangjunha_team3.domain.dto.PostDto;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterRequest;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterResponse;
import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import com.finalproject.hwangjunha_team3.exceptionManager.HospitalReviewAppException;
import com.finalproject.hwangjunha_team3.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    @DisplayName("포스트 작성 성공")
    void post_success() throws Exception {

        PostRegisterRequest postRequest = PostRegisterRequest.builder()
                .title("title_post")
                .body("body_post")
                .build();

        when(postService.post(any()))
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
                .title("title_post")
                .body("body_post")
                .build();

        when(postService.post(any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}