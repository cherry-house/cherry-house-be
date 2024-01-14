package com.cherryhouse.server.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.id < :key order by p.createdDate desc")
    List<Post> findAllByIdLessThanOrderOrderByCreatedDate(Long key, Pageable pageable);

    @Query("select p from Post p order by p.createdDate desc")
    List<Post> findAllOrderByCreatedDate(Pageable pageable);
}