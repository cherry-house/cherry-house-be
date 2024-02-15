package com.cherryhouse.server.chatroom;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.chatroom.chat.Chat;
import com.cherryhouse.server.chatroom.chat.ChatRepository;
import com.cherryhouse.server.chatroom.dto.ChatRoomResponse;
import com.cherryhouse.server.chatroom.entry.ChatRoomEntry;
import com.cherryhouse.server.chatroom.entry.ChatRoomEntryRepository;
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
    private final ChatRoomEntryRepository entryRepository;

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
        enter(chatRoomId, email);

        //TODO: 읽지 않은 채팅 읽음 처리 쿼리 개선
        chatRepository.findAllByChatRoomIdAndSenderEmail(chatRoomId, email)
                .forEach(Chat::read);

        //TODO: 상대방이 접속 중일 경우 재요청
    }

    @Transactional
    public void disconnect(Long chatRoomId, String email){
        validateChatRoom(chatRoomId, email);

        //채팅방 입장 내역 삭제 -> 채팅방 미접속
        ChatRoomEntry entry = entryRepository.findByChatRoomIdAndEmail(chatRoomId, email)
                .orElseThrow(() -> new ApiException(ExceptionCode.CHATROOM_NOT_FOUND));
        entryRepository.delete(entry);
    }

    private void enter(Long chatRoomId, String email) {
        ChatRoomEntry chatRoomEntry = ChatRoomEntry.builder()
                .chatRoomId(chatRoomId)
                .email(email)
                .build();
        entryRepository.save(chatRoomEntry);
    }

    public ChatRoom getChatRoom(Long chatRoomId){
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new ApiException(ExceptionCode.CHATROOM_NOT_FOUND));
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
