package com.cherryhouse.server.post.image;

import com.cherryhouse.server.post.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "image_tb")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String saveImgUrl;

    @Column(nullable = false)
    private String accessImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String createdDate;

    @Builder
    public Image(String saveImgUrl, String accessImgUrl, Post post){
        this.saveImgUrl = saveImgUrl;
        this.accessImgUrl = accessImgUrl;
        this.post = post;
        this.createdDate = String.valueOf(LocalDateTime.now());
    }
}
