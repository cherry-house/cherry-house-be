package com.cherryhouse.server.chatroom.entry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomEntryRepository extends JpaRepository<ChatRoomEntry, Long> {
}
