package com.cherryhouse.server.chat.dto;

import jakarta.validation.constraints.NotNull;

public class ChatRequest {

    public record CreateDto(

            @NotNull(message = "내용은 필수 입력입니다")
            String content,

            String type
    ){}
}
