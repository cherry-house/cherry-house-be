package com.cherryhouse.server.chatroom.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomSessionService {

    private final ChatRoomSessionRepository sessionRepository;
    private static final Integer CHAT_ROOM_MAXIMUM = 2;

    //채팅방 입장 정보 저장
    @Transactional
    public void create(Long chatRoomId, String email){
        //TODO: 존재하는 세션인지 확인
        ChatRoomSession session = ChatRoomSession.builder()
                .chatRoomId(chatRoomId)
                .email(email)
                .build();
        sessionRepository.save(session);
    }

    //채팅방 입장 정보 삭제
    @Transactional
    public void delete(Long chatRoomId, String email){
        //TODO: 존재하는 세션인지 확인
        sessionRepository.deleteByChatRoomIdAndEmail(chatRoomId, email);
    }

    //채팅방 접속 유무 확인
    public Boolean isAllConnected(Long chatRoomId){
        List<ChatRoomSession> sessions = sessionRepository.findByChatRoomId(chatRoomId);
        return sessions.size() == CHAT_ROOM_MAXIMUM;
    }
}