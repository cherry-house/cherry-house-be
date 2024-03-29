package com.cherryhouse.server.post;

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
@Table(name = "post_tb")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private String storeLocationX;
    private String storeLocationY;
    private String createdDate;
    private String updatedDate;

    @ColumnDefault("false")
    private boolean deleted;

    @Builder
    public Post(String title, User user, String content, Category category,
                String storeLocationX, String storeLocationY) {
        this.title = title;
        this.user = user;
        this.content = content;
        this.category = category;
        this.storeLocationX = storeLocationX;
        this.storeLocationY = storeLocationY;
        this.createdDate = String.valueOf(LocalDateTime.now());
    }

    public void update(String title, String content, Category category){
        this.title = title;
        this.content = content;
        this.category = category;
        this.updatedDate = String.valueOf(LocalDateTime.now());
    }

    public void deletePost(){
        this.deleted = true;
        this.title = "삭제된 게시글입니다";
        this.content = "삭제된 게시글입니다.";
    }
}
