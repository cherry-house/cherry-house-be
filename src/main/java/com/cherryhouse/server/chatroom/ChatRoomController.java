package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.chatroom.dto.ChatRequest;
import com.cherryhouse.server.chatroom.dto.ChatResponse;
import com.cherryhouse.server.chatroom.dto.ChatRoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CHATROOM", description = "채팅 API 입니다.")
@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //채팅방 목록 조회
    @Operation(summary = "자신의 채팅방 목록 조회", description = "사용자가 자신의 채팅방 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable){
        ChatRoomResponse.ChatRoomsDto response = chatRoomService.getChatRooms(pageable, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    //채팅방 생성
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @RequestParam(name = "postId") Long postId){
        chatRoomService.create(userPrincipal.getEmail(), postId);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    //채팅 목록 조회
    @Operation(summary = "채팅 목록 조회", description = "사용자가 채팅 목록을 조회합니다.")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChats(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @PathVariable("chatRoomId") Long chatRoomId, Pageable pageable){
        ChatRoomResponse.ChatsDto response = chatRoomService.getChats(pageable, chatRoomId, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    //채팅
    @MessageMapping("/chat.{chatRoomId}")
    @SendTo("/queue/{chatRoomId}")
    public ChatResponse chat(ChatRequest request, @DestinationVariable Long chatRoomId) {
        return chatRoomService.chat(request, chatRoomId);
    }

    //채팅방 연결 끊기
    @Operation(summary = "채팅방 연결 삭제", description = "채팅방 연결을 삭제합니다.")
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<?> disconnect(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @PathVariable("chatRoomId") Long chatRoomId){
        chatRoomService.disconnect(chatRoomId, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
