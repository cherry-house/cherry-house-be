package com.cherryhouse.server.heart;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {


    @Query("select count(h.id) > 0 " +
            "from Heart h " +
            "where h.user.email = :email " +
            "and h.post.id = :postId")
    boolean existsByUserAndPost(@Param("email") String email, @Param("postId")Long postId);

    @Transactional
    @Modifying
    @Query("delete from Heart h " +
            "where h.post.id = :postId " +
            "and h.user = :user")
    void deleteByUserAndPostId(@Param("user") User user, @Param("postId") Long postId);

    @Query("select h.post from Heart h " +
            "where h.user.email = :email " +
            "order by h.id " +
            "desc")
    Page<Post> findPostByUserEmail(@Param("email") String email, Pageable pageable);


}
