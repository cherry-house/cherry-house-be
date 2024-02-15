package com.cherryhouse.server.chat.dto;

import lombok.*;

@Setter
@Getter
public class ChatDto {
    private Long sender;
    private String message;
    private String type;
    private Boolean isRead;

    @Builder
    public ChatDto(Long sender, String message, String type, Boolean isRead) {
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
    }
}