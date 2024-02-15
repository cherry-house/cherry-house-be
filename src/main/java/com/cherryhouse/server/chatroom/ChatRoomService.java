package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.chatroom.chat.Chat;
import com.cherryhouse.server.chatroom.chat.ChatRepository;
import com.cherryhouse.server.chatroom.dto.ChatDto;
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
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomSessionService sessionService;

    public ChatRoomResponse.ChatRoomsDto getChatRooms(Pageable pageable, String email){
        Page<ChatRoom> chatRoomList= chatRoomRepository.findAllOrderByCreatedDate(pageable, email);
        PageData pageData = getPageData(chatRoomList);
        return ChatRoomResponse.ChatRoomsDto.of(pageData, chatRoomList.getContent());
    }

    @Transactional
    public void create(String email, Long postId){
        //TODO: 마감된 post 체크
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
        validateChatRoom(chatRoomId, email);

        Page<Chat> chatList = chatRepository.findAllByChatRoomIdOrderByCreatedDate(chatRoomId, pageable);
        PageData pageData = getPageData(chatList);
        User user = userService.findByEmail(email);

        return ChatRoomResponse.ChatsDto.of(pageData, user, chatList.getContent());
    }

    @Transactional
    public void connect(Long chatRoomId, String email){
        validateChatRoom(chatRoomId, email);

        //채팅방 입장 내역 저장 -> 채팅방 접속
        sessionService.create(chatRoomId, email);

        //채팅 읽음 처리
        //TODO: 읽지 않은 채팅 읽음 처리 쿼리 개선
        chatRepository.findAllByChatRoomIdAndSenderEmail(chatRoomId, email)
                .forEach(Chat::read);
    }

    @Transactional
    public Chat chat(ChatDto chatDto, Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(ExceptionCode.CHATROOM_NOT_FOUND));
        User user = userService.findById(chatDto.getSender());

        Chat chat = Chat.builder()
                .content(chatDto.getMessage())
                .chatRoom(chatRoom)
                .sender(user)
                .type(chatDto.getType())
                .build();

        chatRepository.save(chat);
        return chat;
    }

    @Transactional
    public void disconnect(Long chatRoomId, String email){
        validateChatRoom(chatRoomId, email);

        //채팅방 입장 내역 삭제 -> 채팅방 미접속
        sessionService.delete(chatRoomId, email);
    }

    private void existsChatRoom(Long postId, String email) {
        if (chatRoomRepository.existsByPostIdAndUserEmail(postId, email)) {
            throw new ApiException(ExceptionCode.CHATROOM_EXISTS);
        }
    }

    private void validateChatRoom(Long chatRoomId, String email){
        if (chatRoomRepository.existsByIdAndUserEmail(chatRoomId, email)) {
            throw new ApiException(ExceptionCode.CHATROOM_NOT_FOUND);
        }
    }
}
