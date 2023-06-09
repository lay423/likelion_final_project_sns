package com.finalproject.hwangjunha_team3.service;

import com.finalproject.hwangjunha_team3.domain.*;
import com.finalproject.hwangjunha_team3.domain.dto.*;
import com.finalproject.hwangjunha_team3.exceptionManager.ErrorCode;
import com.finalproject.hwangjunha_team3.exceptionManager.HospitalReviewAppException;
import com.finalproject.hwangjunha_team3.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final AlarmRepository alarmRepository;

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

        Alarm alarm = Alarm.builder()
                .alarmType("NEW_COMMENT_ON_POST")
                .fromUserId(user.getId())
                .targetId(post.getId())
                .text("new comment!")
                .user(post.getUser())
                .build();
        alarmRepository.save(alarm);

        return Comment.of(createdComment);
    }

    public CommentModifyResponse modifyComment(Integer postId, Integer commentId, CommentRequest commentRequest, String userName) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.COMMENT_NOT_FOUND, String.format("commentId %d is not found", commentId)));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        //댓글의 포스트ID가 파라미터와 일치한지 검증
        if (comment.getPost().getId() != postId) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, String.format("postId %d is not match with %d", comment.getPost().getId(), postId));
        }

        //댓글의 userName과 Token의 userName이 일치한지 검증
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with comment %d", userName, commentId));
        }

        comment.setComment(commentRequest.getComment());
        Comment modifiedComment = commentRepository.saveAndFlush(comment);

        return Comment.ofModify(modifiedComment);
    }

    public boolean deleteComment(String userName, Integer postId, Integer commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.COMMENT_NOT_FOUND, String.format("commentId %d is not found", commentId)));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        //댓글의 포스트ID가 파라미터와 일치한지 검증
        if (comment.getPost().getId() != postId) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, String.format("postId %d is not match with %d", comment.getPost().getId(), postId));
        }

        //댓글의 userName과 Token의 userName이 일치한지 검증
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with comment %d", userName, commentId));
        }

        commentRepository.delete(comment);

        return true;
    }

    public Page<CommentResponse> getAllComments(Pageable pageable, Integer postId) {
        Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
        Page<CommentResponse> commentResponses = CommentResponse.toDtoList(comments);
        return commentResponses;
    }

    public boolean like(Integer postId, String userName) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));

        User findUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        Optional<Like> likeOptional = likeRepository.findByPostIdAndUserId(findPost.getId(), findUser.getId());

        if (likeOptional.isPresent()) {
            throw new HospitalReviewAppException(ErrorCode.ALREADY_LIKED, "");
        } else {
            Like like = Like.builder()
                    .user(findUser)
                    .post(findPost)
                    .build();
            likeRepository.save(like);
        }

        Alarm alarm = Alarm.builder()
                .alarmType("NEW_LIKE_ON_POST")
                .fromUserId(findUser.getId())
                .targetId(findPost.getId())
                .text("new like!")
                .user(findPost.getUser())
                .build();
        alarmRepository.save(alarm);

        return true;
    }

    public Integer getLikeCnt(Integer postId) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));

        return likeRepository.countAllByPost(findPost);
    }

    public Page<PostDto> myFeed(Pageable pageable, String userName) {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        Page<Post> posts = postRepository.findAllByUserId(user.getId(), pageable);
        Page<PostDto> postDtos = PostDto.toDtoList(posts);

        return postDtos;
    }

    public Page<AlarmResponse> getAlarm(Pageable pageable, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, String.format("%s not founded", userName)));

        Page<Alarm> alarms = alarmRepository.findAllByUserId(user.getId(), pageable);
        Page<AlarmResponse> alarmResponses = AlarmResponse.toDtoList(alarms);

        return alarmResponses;
    }

}
