package com.cherryhouse.server.chat;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server.chat.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat/{chatRoomId}")
    public void send(@Payload ChatRequest.CreateDto createDto, @DestinationVariable Long chatRoomId,
                     @AuthenticationPrincipal UserPrincipal userPrincipal){
        log.info("chatttt");
        chatService.create(createDto, chatRoomId, userPrincipal.getEmail());
        template.convertAndSend("/topic/" + chatRoomId, "?"); //메시지 전송
    }
}
