package com.cherryhouse.server.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static java.time.LocalTime.now;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post_tb")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    private String storeLocationX;

    private String storeLocationY;

    private String createdDate;

    private String updatedDate;

    @Builder
    public Post(String title, String content, String category, String storeLocationX,
                String storeLocationY, String createdDate, String updatedDate) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.storeLocationX = storeLocationX;
        this.storeLocationY = storeLocationY;
        this.createdDate = String.valueOf(LocalDateTime.now());
        this.updatedDate = updatedDate;
    }

    public void update(String title, String content, String category){
        this.title = title;
        this.content = content;
        this.category = category;
        this.updatedDate = String.valueOf(LocalDateTime.now());
    }
}
