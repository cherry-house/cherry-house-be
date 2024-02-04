package com.cherryhouse.server.chat;

import com.cherryhouse.server.chat.dto.ChatRequest;
import com.cherryhouse.server.chatroom.ChatRoom;
import com.cherryhouse.server.chatroom.ChatRoomService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatRepository chatRepository;

    public Page<Chat> getChats(Pageable pageable, Long chatRoomId){
        return chatRepository.findAllOrderByCreatedDate(pageable, chatRoomId);
    }

    @Transactional
    public void create(ChatRequest.CreateDto createDto, Long chatRoomId, String email){
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        User user = userService.findByEmail(email);

        Chat chat = Chat.builder()
                .content(createDto.content())
                .chatRoom(chatRoom)
                .user(user)
                .type(createDto.type())
                .build();

        chatRepository.save(chat);
    }
}
