package com.cherryhouse.server.chatroom.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c " +
            "where c.chatRoom.id = :chatRoomId " +
            "order by c.createdDate " +
            "desc")
    Page<Chat> findAllByChatRoomIdOrderByCreatedDate(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

    @Query("select c from Chat c " +
            "where c.chatRoom.id = :chatRoomId " +
            "and c.receiver.email = :email " +
            "and c.isRead = false")
    List<Chat> findAllByChatRoomIdAndReceiverEmail(@Param("chatRoomId") Long chatRoomId, @Param("email") String email);
}
