package com.cherryhouse.server.chatroom.dto;

import lombok.*;

@Setter
@Getter
public class ChatDto {
    private String sender;
    private String message;
    private String type;
    private Boolean isRead;

    @Builder
    public ChatDto(String sender, String message, String type, Boolean isRead) {
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
    }
}
