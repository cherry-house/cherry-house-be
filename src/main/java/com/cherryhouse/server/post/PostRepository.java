package com.cherryhouse.server.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p order by p.createdDate desc")
    Page<Post> findAllOrderByCreatedDate(Pageable pageable);
}