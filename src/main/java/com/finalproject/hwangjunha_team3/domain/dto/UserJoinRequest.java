package com.finalproject.hwangjunha_team3.domain.dto;

import com.finalproject.hwangjunha_team3.domain.User;
import com.finalproject.hwangjunha_team3.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;
    private String email;

    public User toEntity(String password){
        return User.builder()
                .userName(this.userName)
                .password(password)
                .emailAddress(this.email)
                .role(UserRole.USER)
                .build();
    }
}
