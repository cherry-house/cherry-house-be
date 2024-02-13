package com.cherryhouse.server.chat;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server.chat.dto.ChatRequest;
import com.cherryhouse.server.chat.dto.Greeting;
import com.cherryhouse.server.chat.dto.HelloMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        log.info("greeting!!!!!!!!!!");
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    /*
    @MessageMapping("/chat")
    public void send(@Payload ChatRequest.CreateDto createDto, @DestinationVariable Long chatRoomId,
                     @AuthenticationPrincipal UserPrincipal userPrincipal){
        log.info("chatttt");
        chatService.create(createDto, chatRoomId, userPrincipal.getEmail());
        template.convertAndSend("/topic/" + chatRoomId, "?"); //메시지 전송
    }
    */
}
