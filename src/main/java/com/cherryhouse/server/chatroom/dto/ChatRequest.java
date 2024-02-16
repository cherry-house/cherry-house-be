package com.cherryhouse.server.chatroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRequest {
    private String accessToken;
    private String message;
    private String type;

    @Builder
    public ChatRequest(String accessToken, String message, String type) {
        this.accessToken = accessToken;
        this.message = message;
        this.type = type;
    }
}
