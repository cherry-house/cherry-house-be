package com.cherryhouse.server.chatroom;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_room_tb")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String createdDate;

    @Builder
    public ChatRoom(Post post, User user) {
        this.post = post;
        this.user = user;
        this.createdDate = String.valueOf(LocalDateTime.now());
    }
}
