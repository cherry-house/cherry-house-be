package com.cherryhouse.server.chatroom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select ch from ChatRoom ch " +
            "where ch.user.email = :email " +
            "order by ch.createdDate " +
            "desc")
    Page<ChatRoom> findAllOrderByCreatedDate(Pageable pageable, @Param("email") String email);

    Boolean existsByPostIdAndUserEmail(@Param("postId") Long postId, @Param("email") String email);

    Boolean existsByIdAndUserEmail(@Param("id") Long id, @Param("email") String email);
}