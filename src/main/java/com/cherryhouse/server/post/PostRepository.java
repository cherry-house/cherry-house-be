package com.cherryhouse.server.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p " +
            "order by p.createdDate " +
            "desc")
    Page<Post> findAllOrderByCreatedDate(Pageable pageable);

    @Query("select p from Post p " +
            "where p.user.email = :email " +
            "and p.id = :id")
    Optional<Post> findByIdAndUserEmail(@Param("id")Long id, @Param("email")String email);
}