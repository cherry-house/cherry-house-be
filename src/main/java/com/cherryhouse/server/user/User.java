package com.cherryhouse.server.user;

import com.cherryhouse.server.post.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImage;

    @Builder
    public User(int id, String email, String password, String username, String profileImage){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = username;
        this.profileImage = profileImage;
    }

    public void updatePassword(String password) { this.password = password ;}



}
