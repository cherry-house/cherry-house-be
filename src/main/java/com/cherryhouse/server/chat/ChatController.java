package com.cherryhouse.server.chat;

import com.cherryhouse.server.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.{chatRoomId}")
    @SendTo("/queue/{chatRoomId}")
    public ChatDto chat(ChatDto chatDto, @DestinationVariable Long chatRoomId) {
        Chat chat = chatService.create(chatDto, chatRoomId);
        return ChatDto.builder()
                .sender(chat.getSender().getId())
                .message(chat.getContent())
                .type(chat.getType())
                .build();
    }
}
