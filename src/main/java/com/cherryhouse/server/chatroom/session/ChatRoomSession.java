package com.cherryhouse.server.chatroom.session;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "session")
public class ChatRoomSession {

    @Id
    private String id;
    private Long chatRoomId;
    private String email;
}
