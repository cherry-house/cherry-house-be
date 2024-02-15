package com.cherryhouse.server.chatroom.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomSessionRepository extends JpaRepository<ChatRoomSession, Long> {

    Optional<ChatRoomSession> findByChatRoomIdAndEmail(@Param("chatRoomId") Long chatRoomId, @Param("email") String email);

    List<ChatRoomSession> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
