package com.sparta.mvm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.mvm.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class PostResponseDto {
    private String msg;
    private int statusCode;
    private Long id;
    @NotBlank(message = "내용을 입력해 주세요")
    private String contents;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    public PostResponseDto(String msg, int statusCode, Long Id, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.msg = msg;
        this.statusCode = statusCode;
        this.id = Id;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

    // 게시글 조회
    public static PostResponseDto toList(String msg, int statusCode, Post post) {
        return PostResponseDto.builder()
                .msg(msg)
                .statusCode(statusCode)
                .id(post.getId())
                //   .username(post.getUser().getUsername())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    //게시글 등록, 수정
    public static PostResponseDto toDto(String msg, int statusCode, Post post) {
        return PostResponseDto.builder()
                .msg(msg)
                .statusCode(statusCode)
                //   .username(post.getUser().getUsername())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    // 게시글 삭제
    public static PostResponseDto toDeleteResponse(String msg, int statusCode) {
        return PostResponseDto.builder()
                .msg(msg)
                .statusCode(statusCode)
                .build();
    }
}
