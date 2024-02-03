package com.cherryhouse.server.post.image;

import com.cherryhouse.server.post.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "image_tb")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Image(String imgUrl, Post post){
        this.imgUrl = imgUrl;
        this.post = post;
    }
}
