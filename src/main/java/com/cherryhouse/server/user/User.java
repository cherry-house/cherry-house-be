package com.cherryhouse.server.user;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.style.Style;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private String profileImage;

    @Column
    private String introduction;




    @Builder
    public User(Long id, String email, String password, String username, String profileImage, String introduction){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = username;
        this.profileImage = profileImage;
        this.introduction = introduction;
    }

    public void updateInfo(String username, String introduction){
        this.name = username;
        this.introduction = introduction;
    }
    public void updateImg(String img) { this.profileImage = img; }
    public void updatePassword(String password){
        this.password = password;
    }
}
