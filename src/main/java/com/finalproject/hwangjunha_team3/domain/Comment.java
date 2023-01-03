package com.finalproject.hwangjunha_team3.domain;

import com.finalproject.hwangjunha_team3.domain.dto.CommentResponse;
import lombok.*;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUserName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
