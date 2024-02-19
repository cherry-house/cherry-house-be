package com.cherryhouse.server.chatroom.chat;

import com.cherryhouse.server.chatroom.ChatRoom;
import com.cherryhouse.server.chatroom.session.ChatRoomSessionService;
import com.cherryhouse.server.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomSessionService sessionService;

    @Transactional
    public Chat create(String msg, ChatRoom chatRoom, User sender, User receiver, String type){
        //상대방도 접속 중이면, isRead true
        Boolean isRead = sessionService.isAllConnected(chatRoom.getId());

        Chat chat = Chat.builder()
                .content(msg)
                .chatRoom(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .type(type)
                .isRead(isRead)
                .build();

        chatRepository.save(chat);
        return chat;
    }

    public void read(Long chatRoomId, String email){
        //TODO: 읽지 않은 채팅 읽음 처리 쿼리 개선
        chatRepository.findAllByChatRoomIdAndReceiverEmail(chatRoomId, email)
                .forEach(Chat::read);
    }
}
