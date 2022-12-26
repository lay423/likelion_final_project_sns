package com.finalproject.hwangjunha_team3.domain.dto;

import com.finalproject.hwangjunha_team3.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostDto {

    private Integer id;
    private String title;
    private String body;
    private String userName;
    private LocalDateTime createdAt;

//    public static PostDto fromEntity(PostEntity postEntity) {
//        return PostDto.builder()
//                .id(postEntity.getId())
//                .title(postEntity.getTitle())
//                .body(postEntity.getBody())
//                .userName(postEntity.getUser().getUserName())
//                .createdAt(postEntity.getCreatedAt())
//                .build();
//    }
//

    public static Page<PostDto> toDtoList(Page<Post> postEntities){
        Page<PostDto> postDtoList = postEntities.map(m -> PostDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .body(m.getBody())
                .userName(m.getUser().getUserName())
                .createdAt(m.getCreatedAt())
                .build());
        return postDtoList;
    }
}
