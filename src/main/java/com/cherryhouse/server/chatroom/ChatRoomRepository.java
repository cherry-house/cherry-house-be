package com.cherryhouse.server.chatroom;

import com.cherryhouse.server.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select ch from ChatRoom ch " +
            "where ch.user.id = :userId " +
            "order by ch.createdDate " +
            "desc")
    Page<Post> findAllOrderByCreatedDate(Pageable pageable, @Param("userId") Long userId);
}