package com.cherryhouse.server.post.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByPostId(@Param("postId") Long postId);

    @Query("select i from Image i " +
            "where i.post.id = :postId " +
            "order by i.createdDate " +
            "desc")
    List<Image> findFirstByPostId(@Param("postId") Long postId); //limit 1

    void deleteByPostId(@Param("postId") Long postId);
}