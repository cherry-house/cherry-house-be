package com.cherryhouse.server.post.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class PostRequest {

    public record CreateDto(

            @NotBlank(message = "제목은 필수 입력입니다")
            String title,

            @NotBlank(message = "카테고리는 필수 입력입니다")
            String category,

            @NotBlank(message = "내용은 필수 입력입니다")
            String content,

            String location,
            List<String> tags,
            List<String> photos
    ){}

    public record UpdateDto(

            @NotBlank(message = "제목은 필수 입력입니다")
            String title,

            @NotBlank(message = "카테고리는 필수 입력입니다")
            String category,

            @NotBlank(message = "내용은 필수 입력입니다")
            String content,

            String location,
            List<String> tags,
            List<String> photos
    ){}
}
