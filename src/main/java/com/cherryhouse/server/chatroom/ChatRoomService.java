package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.TokenProvider;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.chatroom.chat.Chat;
import com.cherryhouse.server.chatroom.chat.ChatRepository;
import com.cherryhouse.server.chatroom.chat.ChatService;
import com.cherryhouse.server.chatroom.dto.ChatRequest;
import com.cherryhouse.server.chatroom.dto.ChatResponse;
import com.cherryhouse.server.chatroom.dto.ChatRoomResponse;
import com.cherryhouse.server.chatroom.session.ChatRoomSessionService;
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
    private final ChatRoomSessionService sessionService;
    private final TokenProvider tokenProvider;

    public ChatRoomResponse.ChatRoomsDto getChatRooms(Pageable pageable, String email){
        Page<ChatRoom> chatRoomList= chatRoomRepository.findAllOrderByCreatedDate(pageable, email);
        PageData pageData = getPageData(chatRoomList);
        return ChatRoomResponse.ChatRoomsDto.of(pageData, chatRoomList.getContent());
    }

    @Transactional
    public void create(String email, Long postId){
        //TODO: 예약 마감된 post 체크
        existsChatRoom(postId, email);

        Post post = postService.getPostById(postId);
        User user = userService.findByEmail(email);

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .user(user)
                .build();
        chatRoomRepository.save(chatRoom);
    }

    public ChatRoomResponse.ChatsDto getChats(Pageable pageable, Long chatRoomId, String email){
        Page<Chat> chatList = chatService.getChats(chatRoomId, pageable);
        PageData pageData = getPageData(chatList);
        User user = userService.findByEmail(email);

        return ChatRoomResponse.ChatsDto.of(pageData, user, chatList.getContent());
    }

    @Transactional
    public void connect(Long chatRoomId, String email){
        //채팅방 입장 내역 저장 -> 채팅방 접속
        sessionService.create(chatRoomId, email);

        //채팅 읽음 처리
        chatService.read(chatRoomId, email);
    }

    @Transactional
    public ChatResponse chat(ChatRequest request, Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(ExceptionCode.CHATROOM_NOT_FOUND));

        //현재 접속 유저가 신청자라면 상대방은 게시글 작성자, 신청자가 아니라면 상대방은 신청자
        User sender = userService.findByEmail(tokenProvider.getEmail(request.getAccessToken()));
        User receiver = sender.getEmail().equals(chatRoom.getUser().getEmail())
                ? chatRoom.getPost().getUser()
                : chatRoom.getUser();

        Chat chat = chatService.create(request.getMessage(), chatRoom, sender, receiver, request.getType());

        return ChatResponse.builder()
                .sender(chat.getSender().getEmail())
                .message(chat.getContent())
                .type(chat.getType())
                .isRead(chat.getIsRead())
                .build();
    }

    @Transactional
    public void disconnect(Long chatRoomId, String email){
        //채팅방 입장 내역 삭제 -> 채팅방 미접속
        sessionService.delete(chatRoomId, email);
    }

    private void existsChatRoom(Long postId, String email) {
        if (chatRoomRepository.existsByPostIdAndUserEmail(postId, email)) {
            throw new ApiException(ExceptionCode.CHATROOM_EXISTS);
        }
    }
}
