package com.finalproject.hwangjunha_team3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostRegisterResponse {
    private String message;
    private long postId;
}
