package com.finalproject.hwangjunha_team3.service;

import com.finalproject.hwangjunha_team3.domain.Post;
import com.finalproject.hwangjunha_team3.domain.dto.PostInquireResponse;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterRequest;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterResponse;
import com.finalproject.hwangjunha_team3.repository.PostRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostRegisterResponse post (PostRegisterRequest request){
        //final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        Post post = postRepository.save(Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .createAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build());
        return new PostRegisterResponse("포스트 등록 완료", post.getId());
    }

    public PostInquireResponse findById(long postsId) {
        Post post = postRepository.findById(postsId).orElseThrow(IllegalArgumentException::new);

        return PostInquireResponse.builder()
                .id(post.getId())
                .body(post.getBody())
                .title(post.getTitle())
                .userName(post.getUserName())
                .lastModifiedAt(post.getLastModifiedAt())
                .createAt(post.getCreateAt())
                .build();
    }
}
