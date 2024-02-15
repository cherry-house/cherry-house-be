package com.cherryhouse.server.chatroom.dto;

import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.chat.Chat;
import com.cherryhouse.server.chatroom.ChatRoom;
import com.cherryhouse.server.user.User;

import java.util.List;

public class ChatRoomResponse {

    public record ChatRoomsDto(
            PageData page,
            List<ChatRoomDto> chatRoomList
    ){
        public static ChatRoomsDto of(
                PageData pageData,
                List<ChatRoom> chatRoomList
        ){
            return new ChatRoomsDto(
                    pageData,
                    chatRoomList.stream()
                            .map(chatRoom -> new ChatRoomDto(chatRoom, chatRoom.getPost().getUser()))
                            .toList()
            );
        }
        //TODO: 마지막 메시지 추가
        public record ChatRoomDto (
                Long id,
                String username,
                String profileImage,
                String message
        ){
            public ChatRoomDto(ChatRoom chatRoom, User user) {
                this(
                        chatRoom.getId(),
                        user.getName(),
                        user.getProfileImage(),
                        null
                );
            }
        }
    }

    public record ChatsDto(
            PageData page,
            UserDto user,
            List<ChatDto> chatList
    ){
        public static ChatsDto of(
                PageData pageData,
                User user,
                List<Chat> chatList
        ){
            return new ChatsDto(
                    pageData,
                    new UserDto(user),
                    chatList.stream()
                            .map(chat -> new ChatDto(chat, user.getId()))
                            .toList()
            );
        }

        public record ChatDto (
                Long id,
                String content,
                String timestamp,
                Boolean isReceived,
                Boolean isRead

        ){
            public ChatDto(Chat chat, Long userId) {
                this(
                        chat.getId(),
                        chat.getContent(),
                        chat.getCreatedDate(),
                        !userId.equals(chat.getSender().getId()),
                        userId.equals(chat.getSender().getId()) || chat.getIsRead()
                );
            }
        }

        public record UserDto (
                String name,
                String image
        ){
            public UserDto(User user) {
                this(
                        user.getName(),
                        user.getProfileImage()
                );
            }
        }
    }
}
