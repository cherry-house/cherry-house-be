package com.cherryhouse.server.style;

import com.cherryhouse.server.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "style_tb")
public class Style{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Style(Long id, String imgUrl, User user){
        this.id = id;
        this.imgUrl = imgUrl;
        this.user = user;
    }
}
