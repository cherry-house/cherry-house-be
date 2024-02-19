package com.cherryhouse.server.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class ChatRequest {
    private String accessToken;
    private String message;
    private String type;
}
