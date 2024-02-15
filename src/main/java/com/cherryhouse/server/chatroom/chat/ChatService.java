package com.cherryhouse.server.chatroom.chat;

import com.cherryhouse.server.chatroom.dto.ChatDto;
import com.cherryhouse.server.chatroom.ChatRoom;
import com.cherryhouse.server.chatroom.ChatRoomService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatRepository chatRepository;

    @Transactional
    public Chat create(ChatDto chatDto, Long chatRoomId){
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        User user = userService.findById(chatDto.getSender());

        Chat chat = Chat.builder()
                .content(chatDto.getMessage())
                .chatRoom(chatRoom)
                .sender(user)
                .type(chatDto.getType())
                .build();

        chatRepository.save(chat);
        return chat;
    }
}
