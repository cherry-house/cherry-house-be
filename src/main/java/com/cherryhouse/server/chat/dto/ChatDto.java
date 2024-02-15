package com.cherryhouse.server.chat.dto;

import lombok.*;

@Setter
@Getter
public class ChatDto {
    private Long sender;
    private String message;
    private String type;

    @Builder
    public ChatDto(Long sender, String message, String type) {
        this.sender = sender;
        this.message = message;
        this.type = type;
    }
}
