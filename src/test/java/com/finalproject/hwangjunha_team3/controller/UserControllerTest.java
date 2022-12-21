package com.finalproject.hwangjunha_team3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.hwangjunha_team3.domain.dto.UserDto;
import com.finalproject.hwangjunha_team3.domain.dto.UserJoinRequest;
import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import com.finalproject.hwangjunha_team3.exceptionManager.HospitalReviewAppException;
import com.finalproject.hwangjunha_team3.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    UserJoinRequest userJoinRequest = UserJoinRequest.builder()
            .userName("junha")
            .password("1234")
            .email("junha@gamil.com")
            .build();

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_success() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("kyeongrok")
                .password("1q2w3e4r")
                .email("oceanfog1@gmail.com")
                .build();


        when(userService.join(any())).thenReturn(mock(UserDto.class));
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 실패")
    void join_fail() throws Exception {


        when(userService.join(any())).thenThrow(new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 실패 - id 없음")
    @WithMockUser
    void login_fail1() throws Exception {
        String id = "junha";
        String password = "1234";
        when(userService.login(any(), any())).thenThrow(new HospitalReviewAppException(ErrorCode.NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password 잘못 입력")
    @WithMockUser
    void login_fail2() throws Exception {
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("kyeongrok")
                .password("1q2w3e4r")
                .email("oceanfog1@gmail.com")
                .build();


//        when(userService.join(any())).thenReturn(mock(UserDto.class));
//        mockMvc.perform(post("/api/v1/users/join")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
//                .andDo(print())
//                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}