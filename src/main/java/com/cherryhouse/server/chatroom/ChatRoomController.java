package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.chatroom.chat.Chat;
import com.cherryhouse.server.chatroom.chat.ChatService;
import com.cherryhouse.server.chatroom.dto.ChatDto;
import com.cherryhouse.server.chatroom.dto.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable){
        ChatRoomResponse.ChatRoomsDto response = chatRoomService.getChatRooms(pageable, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @RequestParam(name = "postId") Long postId){
        chatRoomService.create(userPrincipal.getEmail(), postId);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChats(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @PathVariable("chatRoomId") Long chatRoomId, Pageable pageable){
        ChatRoomResponse.ChatsDto response = chatRoomService.getChats(pageable, chatRoomId, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

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
