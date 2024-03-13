package com.cherryhouse.server.chatroom.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class ChatResponse {
    private String sender;
    private String message;
    private String type;
    private Boolean isRead;
}
