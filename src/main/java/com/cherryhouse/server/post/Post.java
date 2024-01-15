package com.cherryhouse.server.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_tb")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Builder
    public Post(String title, String content, Category category, String storeLocationX, String storeLocationY) {
        this.title = title;
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
}
