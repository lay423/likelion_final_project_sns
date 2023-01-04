package com.finalproject.hwangjunha_team3.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.hwangjunha_team3.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;

    public static Page<CommentResponse> toDtoList(Page<Comment> comments) {
        return comments.map(m -> CommentResponse.builder()
                .id(m.getId())
                .comment(m.getComment())
                .userName(m.getUser().getUserName())
                .postId(m.getPost().getId())
                .createdAt(m.getCreatedAt())
                .build());
    }
}
