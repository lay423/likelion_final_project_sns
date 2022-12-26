package com.finalproject.hwangjunha_team3.service;

import com.finalproject.hwangjunha_team3.domain.User;
import com.finalproject.hwangjunha_team3.domain.UserJwtDto;
import com.finalproject.hwangjunha_team3.domain.dto.UserDto;
import com.finalproject.hwangjunha_team3.domain.dto.UserJoinRequest;
import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import com.finalproject.hwangjunha_team3.exceptionManager.HospitalReviewAppException;
import com.finalproject.hwangjunha_team3.repository.UserRepository;
import com.finalproject.hwangjunha_team3.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;

    private final long expiredTimeMs = 1000L * 60 * 60l;

    public UserDto join(UserJoinRequest request) {
        // 비즈니스 로직 - 회원 가입
        // 회원 userName(id) 중복 Check
        // 중복이면 회원가입 x --> Exception(예외)발생
        // 있으면 에러처리
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user ->{
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME,
                            String.format("DUPLICATED_USER_NAME UserName:%s", request.getUserName()));
                });

        // 회원가입 .save()
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }

    public String login(String userName, String password) {

        //userName 있는지 여부 확인
        //없으면 NOT_FOUND 에러 발생
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s는 가입된 적이 없습니다.", userName)));

        //password 일치 하는지 여부 확인
        if(!encoder.matches(password, user.getPassword()))
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD, String.format("id또는 password가 잘못 됐습니다."));

        return JwtTokenUtil.createToken(userName, secretKey, expiredTimeMs);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, ""));
    }

    public UserJwtDto loadUserByUsername(String userName) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUserName(userName)
                .orElseThrow(()->new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND,userName+ "이 없습니다." ));
        UserJwtDto user = UserJwtDto.fromEntity(userEntity);
        return user;
    }
}
