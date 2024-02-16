package com.cherryhouse.server.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRequest {
    private String sender;
    private String message;
    private String type;

    @Builder
    public ChatRequest(String sender, String message, String type) {
        this.sender = sender;
        this.message = message;
        this.type = type;
    }
}
