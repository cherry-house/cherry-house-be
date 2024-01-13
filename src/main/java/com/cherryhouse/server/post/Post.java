package com.cherryhouse.server.post;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault(value = "now()")
    @Temporal(TemporalType.TIMESTAMP)
    private String createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private String updatedDate;

    @Builder
    public Post(String title, String content, String category, String storeLocationX,
                String storeLocationY, String createdDate, String updatedDate) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.storeLocationX = storeLocationX;
        this.storeLocationY = storeLocationY;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
