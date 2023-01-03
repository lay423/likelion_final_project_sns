package com.finalproject.hwangjunha_team3.service;

import com.finalproject.hwangjunha_team3.domain.Comment;
import com.finalproject.hwangjunha_team3.domain.Post;
import com.finalproject.hwangjunha_team3.domain.User;
import com.finalproject.hwangjunha_team3.domain.dto.*;
import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import com.finalproject.hwangjunha_team3.exceptionManager.HospitalReviewAppException;
import com.finalproject.hwangjunha_team3.repository.CommentRepository;
import com.finalproject.hwangjunha_team3.repository.PostRepository;
import com.finalproject.hwangjunha_team3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Value("${jwt.token.secret}")
    private String secretKey;

    public PostDto post(PostRegisterRequest request, String username) {
        User userEntity = userRepository.findByUserName(username)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", username)));

        Post post = postRepository.save(Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(userEntity)
                .build());
        PostDto postDto = PostDto.builder()
                .id(post.getId())
                .build();
        return postDto;
    }


    public PostDto findById(Integer postsId) {
        Post post = postRepository.findById(postsId)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, String.format("%d의 포스트가 없습니다.", postsId)));

        return PostDto.builder()
                .id(post.getId())
                .body(post.getBody())
                .title(post.getTitle())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public Page<PostDto> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostDto> postDtos = PostDto.toDtoList(posts);
        return postDtos;
    }

    @Transactional
    public boolean delete(String name, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        User userEntity = userRepository.findByUserName(name)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", name)));

        if (!Objects.equals(post.getUser().getUserName(), name)) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", name, postId));
        }
        postRepository.delete(post);
        return true;
    }


    public Post modify(String name, Integer id, String title, String body) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", id)));
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", name)));

        Integer userId = user.getId();
        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, id));
        }
        post.setTitle(title);
        post.setBody(body);
        return postRepository.saveAndFlush(post);
    }

    public CommentResponse comment(CommentRequest commentRequest, String userName, Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", id)));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        Comment createdComment = Comment.builder()
                .comment(commentRequest.getComment())
                .post(post)
                .user(user)
                .build();
        commentRepository.save(createdComment);

        return Comment.of(createdComment);
    }

    public CommentResponse modifyComment(CommentRequest commentRequest, String userName, Integer id) {
        return null;
    }

    public CommentDeleteResponse deleteComment(String userName, Integer id) {
        return null;
    }
}
