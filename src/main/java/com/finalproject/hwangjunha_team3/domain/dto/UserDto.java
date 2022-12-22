package com.finalproject.hwangjunha_team3.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private String email;
}