package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.chatroom.dto.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

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
}
