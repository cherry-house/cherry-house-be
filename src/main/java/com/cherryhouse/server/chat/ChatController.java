package com.cherryhouse.server.chat;

import com.cherryhouse.server.chat.dto.ChatDto;
import com.cherryhouse.server.chat.dto.Greeting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.{chatRoomId}")
    @SendTo("/queue/{chatRoomId}")
    public Greeting greeting(ChatDto chatDto, @DestinationVariable Long chatRoomId) throws Exception {
        Thread.sleep(1000);
        return new Greeting(HtmlUtils.htmlEscape(chatDto.getMessage()) + ":" + chatRoomId);
    }
}
