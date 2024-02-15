package com.cherryhouse.server.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c " +
            "where c.chatRoom.id = :chatRoomId " +
            "order by c.createdDate " +
            "desc")
    Page<Chat> findAllByChatRoomIdOrderByCreatedDate(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
}
