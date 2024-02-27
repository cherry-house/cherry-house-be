package com.cherryhouse.server.chatroom.chat;

import com.cherryhouse.server.chatroom.ChatRoom;
import com.cherryhouse.server.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_tb")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @ColumnDefault("false")
    private Boolean isRead;

    private String type;
    private String createdDate;

    @Builder
    public Chat(String content, ChatRoom chatRoom, User sender, User receiver, Boolean isRead, String type) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.receiver = receiver;
        this.isRead = isRead;
        this.type = type;
        this.createdDate = String.valueOf(LocalDateTime.now());
    }

    public void read(){
        this.isRead = true;
    }
}
