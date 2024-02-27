package com.cherryhouse.server.chatroom.session;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomSessionRepository extends MongoRepository<ChatRoomSession, Long> {

    List<ChatRoomSession> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    void deleteByChatRoomIdAndEmail(Long chatRoomId, String email);
}
