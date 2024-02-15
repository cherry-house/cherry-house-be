package com.cherryhouse.server.user.style;

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
    private String saveImgUrl;

    @Column(nullable = false)
    private String accessImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Style(String saveImgUrl, String accessImgUrl, User user){
        this.saveImgUrl = saveImgUrl;
        this.accessImgUrl = accessImgUrl;
        this.user = user;
    }
}
