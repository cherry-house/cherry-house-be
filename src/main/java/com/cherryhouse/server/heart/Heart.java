package com.cherryhouse.server.heart;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "heart_tb")
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Heart(Post post, User user){
        this.post = post;
        this.user = user;
    }
}
