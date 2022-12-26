package com.finalproject.hwangjunha_team3.service;

import com.finalproject.hwangjunha_team3.domain.Post;
import com.finalproject.hwangjunha_team3.domain.User;
import com.finalproject.hwangjunha_team3.domain.dto.PostDto;
import com.finalproject.hwangjunha_team3.domain.dto.PostInquireResponse;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterRequest;
import com.finalproject.hwangjunha_team3.domain.dto.PostRegisterResponse;
import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import com.finalproject.hwangjunha_team3.exceptionManager.HospitalReviewAppException;
import com.finalproject.hwangjunha_team3.repository.PostRepository;
import com.finalproject.hwangjunha_team3.repository.UserRepository;
import com.finalproject.hwangjunha_team3.utils.JwtTokenUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Value("${jwt.token.secret}")
    private String secretKey;

    public PostDto post (PostRegisterRequest request){

        Post post = postRepository.save(Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .build());
        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .build();
        return postDto;
    }

    public PostInquireResponse findById(long postsId) {
        Post post = postRepository.findById(postsId).orElseThrow(IllegalArgumentException::new);

        return PostInquireResponse.builder()
                .id(post.getId())
                .body(post.getBody())
                .title(post.getTitle())
                .userName(post.getUser().getUserName())
                .lastModifiedAt(post.getLastModifiedAt())
                .createAt(post.getCreatedAt())
                .build();
    }

    public Page<PostDto> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostDto> postDtos = PostDto.toDtoList(posts);
        return postDtos;
    }
}
