package com.cherryhouse.server.chatroom.session;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash
public class ChatRoomSession {

    @Id
    private String id;

    private Long chatRoomId;

    private String email;

    @Builder
    public ChatRoomSession(Long chatRoomId, String email) {
        this.chatRoomId = chatRoomId;
        this.email = email;
    }
}
