package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.chat.Chat;
import com.cherryhouse.server.chat.ChatService;
import com.cherryhouse.server.chatroom.dto.ChatRoomResponse;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherryhouse.server._core.util.PageData.getPageData;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final PostService postService;
    private final UserService userService;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomResponse.ChatRoomsDto getChatRooms(Pageable pageable, String email){
        Page<ChatRoom> chatRoomList= chatRoomRepository.findAllOrderByCreatedDate(pageable, email);
        PageData pageData = getPageData(chatRoomList);
        return ChatRoomResponse.ChatRoomsDto.of(pageData, chatRoomList.getContent());
    }

    @Transactional
    public void create(String email, Long postId){
        validateChatRoom(email, postId);

        Post post = postService.getPostById(postId);
        User user = userService.findByEmail(email);

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .user(user)
                .build();
        chatRoomRepository.save(chatRoom);
    }

    public ChatRoomResponse.ChatsDto getChats(Pageable pageable, Long chatRoomId, String email){
        //TODO: 읽음 처리
        Page<Chat> chatList = chatService.getChats(pageable, chatRoomId);
        PageData pageData = getPageData(chatList);
        User user = userService.findByEmail(email);
        return ChatRoomResponse.ChatsDto.of(pageData, user, chatList.getContent());
    }

    public ChatRoom getChatRoom(Long chatRoomId){
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new ApiException(ExceptionCode.CHATROOM_NOT_FOUND));
    }

    private void validateChatRoom(String email, Long postId) {
        if (chatRoomRepository.existsByPostIdAndUserEmail(postId, email)) {
            throw new ApiException(ExceptionCode.CHATROOM_EXISTS);
        }
    }
}
