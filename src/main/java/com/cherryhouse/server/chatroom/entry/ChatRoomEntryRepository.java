package com.cherryhouse.server.chatroom.entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomEntryRepository extends JpaRepository<ChatRoomEntry, Long> {

    Optional<ChatRoomEntry> findByChatRoomIdAndEmail(@Param("chatRoomId") Long chatRoomId, @Param("email") String email);
}
