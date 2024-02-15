package com.cherryhouse.server.chatroom.chat;

import com.cherryhouse.server._core.security.TokenProvider;
import com.cherryhouse.server.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Objects;

@RequiredArgsConstructor
@Configuration
public class StompConnectionInterceptor implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        //토큰 검증
        String accessToken = accessor.getFirstNativeHeader("Authorization");
        tokenProvider.validateToken(accessToken);

        if(command == StompCommand.CONNECT){
            //채팅룸 연결
            Long chatRoomId = getChatRoomId(accessor);
            String email = tokenProvider.getEmail(accessToken);
            chatRoomService.connect(chatRoomId, email);
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }

    private static Long getChatRoomId(StompHeaderAccessor accessor) {
        return Long.valueOf(
                Objects.requireNonNull(
                        accessor.getFirstNativeHeader("chatRoomId")
                )
        );
    }
}
