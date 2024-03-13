package com.cherryhouse.server._core.config;

import com.cherryhouse.server.chatroom.chat.StompConnectionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompConnectionInterceptor stompInterceptor;

    //STOMP 엔드 포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").withSockJS();
    }

    //메시지 브로커 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    //인터셉터를 등록하여 STOMP 메시지 핸들링
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompInterceptor);
    }
}
