package com.cherryhouse.server.post.posttag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findByPostId(Long postId);

    void deleteByPostId(Long postId);
}
