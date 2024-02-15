package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.chat.Chat;
import com.cherryhouse.server.chat.ChatRepository;
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

import java.util.List;

import static com.cherryhouse.server._core.util.PageData.getPageData;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final PostService postService;
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomResponse.ChatRoomsDto getChatRooms(Pageable pageable, String email){
        Page<ChatRoom> chatRoomList= chatRoomRepository.findAllOrderByCreatedDate(pageable, email);
        PageData pageData = getPageData(chatRoomList);
        return ChatRoomResponse.ChatRoomsDto.of(pageData, chatRoomList.getContent());
    }

    @Transactional
    public void create(String email, Long postId){
        //TODO: 마감된 post 체크
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
        //TODO: user, chatroom 권한 체크

        Page<Chat> chatList = chatRepository.findAllByChatRoomIdOrderByCreatedDate(chatRoomId, pageable);
        PageData pageData = getPageData(chatList);
        User user = userService.findByEmail(email);
        return ChatRoomResponse.ChatsDto.of(pageData, user, chatList.getContent());
    }

    @Transactional
    public void connect(Long chatRoomId, String email){
        //TODO: user, chatroom 권한 체크

        //TODO: 입장 내역 저장
        //TODO: 읽지 않은 채팅 읽음 처리 쿼리 개선
        chatRepository.findAllByChatRoomIdAndSenderEmail(chatRoomId, email)
                .forEach(Chat::read);
        //TODO: 상대방이 접속 중일 경우 재요청
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
